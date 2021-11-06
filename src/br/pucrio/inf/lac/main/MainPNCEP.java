package br.pucrio.inf.lac.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.pucrio.inf.lac.auxiliar.StaticLibrary;
import br.pucrio.inf.lac.model.Inspector;
import ckafka.data.Swap;
import ckafka.data.SwapData;
import main.java.application.ModelApplication;

/**
 * @author Gabriel & Matheus
 *
 */
public class MainPNCEP extends ModelApplication  implements UpdateListener {
	private Swap swap;
	/** CEP runtime object */
	private EPRuntime cepRT;

	/**
	 * Constructor
	 */
	public MainPNCEP() {
		this.swap = new Swap(new ObjectMapper());

		// create CEP configuration and engine
		Configuration cepConfig = new Configuration();
		cepConfig.addEventType("Inspector", Inspector.class.getName());
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
		cepRT = cep.getEPRuntime();
		EPAdministrator cepAdm = cep.getEPAdministrator();
		// up right    = -22.927271836395280, -43.17771782650461
		// botton left = -22.946273929306827, -43.19634344082100
		String cepStatement = "select * from Inspector "
				     + "having Inspector.latitude > -22.92727183639528 "
				     + "or Inspector.longitude    > -43.177717826504610 "
				     + "or Inspector.latitude     < -22.946273929306827 "
				     + "or Inspector.longitude    < -43.19634344082100";
		EPStatement cepInspector = cepAdm.createEPL(cepStatement);
		cepInspector.addListener(this);
	}

	/**
	 * Main function
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
    	// creating missing environment variable
		Map<String,String> env = new HashMap<String, String>();
		env.putAll(System.getenv());
		if(System.getenv("app.consumer.topics") == null) 			env.put("app.consumer.topics", "GroupReportTopic");
		if(System.getenv("app.consumer.auto.offset.reset") == null) env.put("app.consumer.auto.offset.reset", "latest");
		if(System.getenv("app.consumer.bootstrap.servers") == null) env.put("app.consumer.bootstrap.servers", "127.0.0.1:9092");
		if(System.getenv("app.consumer.group.id") == null) 			env.put("app.consumer.group.id", "gw-consumer");
		if(System.getenv("app.producer.bootstrap.servers") == null) env.put("app.producer.bootstrap.servers", "127.0.0.1:9092");
		if(System.getenv("app.producer.retries") == null) 			env.put("app.producer.retries", "3");
		if(System.getenv("app.producer.enable.idempotence") == null)env.put("app.producer.enable.idempotence", "true");
		if(System.getenv("app.producer.linger.ms") == null) 		env.put("app.producer.linger.ms", "1");
		if(System.getenv("app.producer.acks") == null) 				env.put("app.producer.acks", "all");
		try {
			StaticLibrary.setEnv(env);
		} catch (Exception e) {
			e.printStackTrace();
		}

		new MainPNCEP();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void recordReceived(ConsumerRecord record) {
		System.out.println(String.format("Mensagem recebida de %s", record.key()));        
		try {
			SwapData data = swap.SwapDataDeserialization((byte[]) record.value());
			Double latitude = Double.valueOf(String.valueOf(data.getContext().get("latitude")));
			Double longitude = Double.valueOf(String.valueOf(data.getContext().get("longitude")));
			System.out.println(String.format("Coordenadas = %f (lat), %f (long)", latitude, longitude));
			generateInspectorLocationCEPEvent(new Inspector(new Date(), latitude, longitude, (String)record.key()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a unicast message to a inspector
	 * @param uuid
	 * @param message
	 */
	private void sendUnicastMessage(String uuid, String messageText) {
		try {
			sendRecord(createRecord("PrivateMessageTopic", uuid,
	                              swap.SwapDataSerialization(createSwapData(messageText))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param inspectort an inspector with lat/long and UUID
	 */
	private void generateInspectorLocationCEPEvent(Inspector inspector) {
		// up right    = -22.927271836395280, -43.17771782650461
		// botton left = -22.946273929306827, -43.19634344082100
		if(inspector.getLatitude()  > -22.92727183639528  ||
		   inspector.getLongitude() > -43.177717826504610 ||
		   inspector.getLatitude()  < -22.946273929306827  ||
		   inspector.getLongitude() < -43.19634344082100)
			logger.info("Deveria disparar um alerta agora para " + inspector.getUuid());
		cepRT.sendEvent(inspector);
	}

	/**
	 * CEP event listener
	 * @param newEvents new events
	 * @param oldEvents old events
	 */
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		logger.info	("Event received: " + newEvents[0].getUnderlying());
		Inspector inspector = (Inspector) newEvents[0].getUnderlying();
		String messageText = String.format("Fiscal %s, você está fora de sua área", inspector.getUuid().toString());
		String uuid = inspector.getUuid().toString();
		sendUnicastMessage(uuid, messageText);
	}
}
