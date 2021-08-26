package json;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.Block;
import model.BlockChain;
import model.Peer;

public class JsonMessage {
	
	public String jsonConnectMessage(String localhost,String userName) { 
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "connect");
		job.add("localhost",localhost);
		job.add("userName", userName);
		return changeJsonToString(job.build());
	}
	
	public String jsonBlockMinedMessage(Block block) {
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
	
	public String jsonRequestBlockNumMessage() {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "requestBlockNum");
		return changeJsonToString(job.build());
	}
	
	public String jsonResPonseBlockNumMessage() {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "responseBlockNum");
		job.add("userName", Peer.myPeer.getUserName());
		job.add("blockNum", BlockChain.blockList.getBlockNum());
		return changeJsonToString(job.build());
	}
	
	public String jsonRequestLeaderBlockMessage() {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "requestLeaderBlock");
		job.add("userName", Peer.myPeer.getUserName());
		return changeJsonToString(job.build());
	}
	
	public String jsonResponseLeaderBlockMessage(Block block) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "responseLeaderBlock");
		job.add("blockNum", block.getNum());
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
