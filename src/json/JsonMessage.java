package json;

import java.io.StringWriter;
import java.security.PublicKey;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.bouncycastle.util.encoders.Base64;

import model.Block;
import model.BlockChain;
import model.Peer;
import model.Transaction;
import model.TransactionInput;
import util.Encoding;

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
	
	public String jsonRequestITXO(PublicKey recipient) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "requestITXO");
		job.add("recipient",Encoding.encodeKey(recipient));
		return changeJsonToString(job.build());
	}
	
	public String jsonResponseITXO(TransactionInput itxo) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "responseITXO");
		job.add("miner",itxo.getMiner());
		job.add("utxoHash", itxo.getUtxoHash());
		job.add("inputValue", itxo.getInputValue()+""); // 주의! float -> 문자열 변환
		job.add("itxoHash", itxo.getItxoHash());
		return changeJsonToString(job.build());
	}
	
	public String jsonSendTransaction(Transaction tx) {
		JsonArrayBuilder minerArr = Json.createArrayBuilder();
		JsonArrayBuilder utxoHashArr = Json.createArrayBuilder();
		JsonArrayBuilder inputValueArr = Json.createArrayBuilder();
		
		for(TransactionInput itxo : tx.getItxoList()) {
			minerArr.add(itxo.getMiner());
			utxoHashArr.add(itxo.getUtxoHash());
			inputValueArr.add(itxo.getInputValue()+"");
		}
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "transaction");
		job.add("sender", Encoding.encodeKey(tx.getSender()));
		job.add("recipient", Encoding.encodeKey(tx.getRecipient()));
		job.add("value",tx.getValue()+"");
		job.add("signature",Encoding.encodeSignature(tx.getSignature()));
		job.add("txHash", tx.getHash());
		job.add("minerArr", minerArr);
		job.add("inputValueArr", inputValueArr);
		job.add("utxoHashArr", utxoHashArr);
		return changeJsonToString(job.build());
	}
	
	public String jsonDeleteUTXO(TransactionInput itxo) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("identifier", "deleteUTXO");
		job.add("utxoHash", itxo.getUtxoHash());
		return changeJsonToString(job.build());
	}
	
	private String changeJsonToString(JsonObject obj) {
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).writeObject(obj);
		return sw.toString();
	}
}
