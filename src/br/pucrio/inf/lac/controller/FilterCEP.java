package br.pucrio.inf.lac.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;

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
import br.pucrio.inf.lac.model.Fence;
import br.pucrio.inf.lac.model.Inspector;
import br.pucrio.inf.lac.storage.Database;
import br.pucrio.inf.lac.storage.Storage;
import ckafka.data.Swap;
import ckafka.data.SwapData;
import main.java.application.ModelApplication;

/**
 * @author Gabriel & Matheus
 *
 */
public class FilterCEP extends ModelApplication  implements UpdateListener {
	private Swap swap;
	/** CEP runtime object */
	private EPRuntime cepRT;
	private static Storage dbConnection;
	private float up;
	private float down;
	private float right;
	private float left;
	/**
	 * Constructor
	 */
	public FilterCEP(float pos_y, float in_pos_y, float pos_x, float in_pos_x) {
		this.swap = new Swap(new ObjectMapper());
		dbConnection = new Storage();
		// set CEP fence values
		this.up = pos_y;
		this.down = in_pos_y;
		this.right = pos_x;
		this.left = in_pos_x;
		// create CEP configuration and engine
		Configuration cepConfig = new Configuration();
		cepConfig.addEventType("Inspector", Inspector.class.getName());
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
		cepRT = cep.getEPRuntime();
		EPAdministrator cepAdm = cep.getEPAdministrator();
		String cepStatement = "select * from Inspector "
				     + "having Inspector.latitude > "+ this.up 
				     + "or Inspector.longitude    > "+ this.right
				     + "or Inspector.latitude     < "+ this.down
				     + "or Inspector.longitude    < "+ this.left;
		EPStatement cepInspector = cepAdm.createEPL(cepStatement);
		cepInspector.addListener(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void recordReceived(ConsumerRecord record) {
		System.out.println(String.format("Mensagem recebida de %s", record.key()));        
		try {
			SwapData data = swap.SwapDataDeserialization((byte[]) record.value());
			Double latitude = Double.valueOf(String.valueOf(data.getContext().get("latitude")));
			Double longitude = Double.valueOf(String.valueOf(data.getContext().get("longitude")));
	        String id = String.valueOf(data.getContext().get("ID"));
	        String date = String.valueOf(data.getContext().get("date"));
			System.out.println(String.format("Coordenadas = %f (lat), %f (long)", latitude, longitude));
			//	store new position in database        
	        dbConnection.AddNewPosition(id, date, latitude, longitude);
	        // generate CEP inspector
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
		if(inspector.getLatitude()  > this.up  ||
		   inspector.getLongitude() > this.right ||
		   inspector.getLatitude()  < this.down  ||
		   inspector.getLongitude() < this.left)
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
