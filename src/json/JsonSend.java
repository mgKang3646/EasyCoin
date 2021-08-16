package json;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.Block;

public class JsonSend {
	
	public String jsonConnectMessage(String localhost,String userName) { 
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "connect");
		job.add("localhost",localhost);
		job.add("userName", userName);
		return changeJsonToString(job.build());
	}
	
	public String jsonBlockVerifyMessage(Block block) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "minedBlock");
		job.add("blockNum", block.getNum());
		job.add("nonce", block.getNonce());
		job.add("timestamp", block.getTimestamp());
		job.add("previousHash", block.getPreviousBlockHash());
		job.add("hash", block.getHash());
		return changeJsonToString(job.build());
	}
	
	public String jsonVerifiedResultMessage(boolean result) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "verifyResult");
		job.add("verifyResult", result);
		return changeJsonToString(job.build());
	}
	
	private String changeJsonToString(JsonObject obj) {
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).writeObject(obj);
		return sw.toString();
	}

}
