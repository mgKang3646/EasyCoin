package util;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonSend {
	
	public String sendLocalhost(String localhost) { 
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("order", "makePeerThread");
		job.add("localhost",localhost);
		return changeJsonToString(job.build());
		
	}
	
	private String changeJsonToString(JsonObject obj) {
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).writeObject(obj);
		return sw.toString();
	}

}
