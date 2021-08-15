package json;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.Block;

public class JsonSend {
	
	public String jsonConnectMessage(String localhost) { 
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "connect");
		job.add("localhost",localhost);
		return changeJsonToString(job.build());
		
	}
	
	public String jsonBlockVerifyMessage(Block block) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "minedBlock");
		job.add("nonce", block.getNonce());
		job.add("timestamp", block.getTimestamp());
		job.add("previousHash", block.getPreviousBlockHash());
		job.add("hash", block.getHash());
		return changeJsonToString(job.build());
	}
	
	private String changeJsonToString(JsonObject obj) {
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).writeObject(obj);
		return sw.toString();
	}

}
