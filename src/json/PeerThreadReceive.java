package json;

import java.io.BufferedReader;

import javax.json.Json;
import javax.json.JsonObject;

import factory.JsonFactory;
import model.BlockVerify;
import model.Peer;
import model.PeerList;

public class PeerThreadReceive implements JsonReceive{
	private Peer peer;
	private BlockVerify blockVerify;
	private PeerList peerList;
	private JsonObject jsonObject;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	

	public PeerThreadReceive(Peer peer) {
		this.peer = peer;
		this.blockVerify = peer.getBlockchain().getBlockVerify();
		this.peerList = peer.getPeerList();
		this.jsonFactory = new JsonFactory();
		this.jsonSend = jsonFactory.getJsonSend();
	}
	
	public void read(BufferedReader bufferedReader) {
		setJsonObject(bufferedReader);
		processJsonQuery();
	}
	
	private void setJsonObject(BufferedReader bufferedReader) {
		jsonObject = Json.createReader(bufferedReader).readObject();
	}

	private void processJsonQuery() {
		String key = jsonObject.getString("identifier");
		
		switch(key) {
			case "minedBlock" : verifyBlock();// 1. 검증 2. 검증결과보내기 3. 다른 Peer 검증결과 확보 4. 과반이 넘으면 검증완료.
			case "verifiedResult" : getVerifyResult();
			default : break;
		}
	}
	
	private void verifyBlock() {
		blockVerify.doVerify(jsonObject);
		jsonSend.jsonVerifiedResultMessage(blockVerify.isTmpBlockValid());
	}
	
	private void getVerifyResult() {
		boolean verifyResult = jsonObject.getBoolean("verifyResult");
		blockVerify.setTotal(peerList.getSize());
		blockVerify.handleVerifyResult(verifyResult);
	}
}
