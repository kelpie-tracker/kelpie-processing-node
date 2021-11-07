/**
 * A command line Processing Node example<br>
 * Sends and receives messages: unicast and groupcast
 * May need a GroupDefiner
 */
package br.pucrio.inf.lac.main;

import java.awt.TextField;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.pucrio.inf.lac.auxiliar.StaticLibrary;
import br.pucrio.inf.lac.controller.FilterCEP;
import br.pucrio.inf.lac.model.Fence;
import br.pucrio.inf.lac.storage.Storage;
import br.pucrio.inf.lac.model.MyRunnable;
import ckafka.data.Swap;
import ckafka.data.SwapData;
import main.java.application.ModelApplication;

/**
 * @author Gabriel & Matheus
 *
 */
public class MainPN {
	private static Storage db;
	/**
	 * Constructor
	 */
	public MainPN() {		

	}
	
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
		// get the geographic areas
		db = new Storage();
		ArrayList<Fence> resultGeo = db.GetGeoFences();
		for(int i=0; i<resultGeo.size(); i++){
			Fence f = resultGeo.get(i);
			Runnable r = new MyRunnable(i,f.GetPosUp(),f.GetPosDown(),f.GetPosRight(),f.GetPosLeft());
			new Thread(r).start();
        }		
	}

}
