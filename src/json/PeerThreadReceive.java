package json;

import java.io.BufferedReader;

import javax.json.Json;
import javax.json.JsonObject;

import factory.JsonFactory;
import model.BlockVerify;
import model.Peer;
import model.PeerList;
import model.ServerListener;

public class PeerThreadReceive implements JsonReceive{
	private Peer peer;
	private ServerListener serverListener;
	private BlockVerify blockVerify;
	private PeerList peerList;
	private JsonObject jsonObject;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	

	public PeerThreadReceive(Peer peer) {
		this.peer = peer;
		this.serverListener = peer.getServerListener();
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
		System.out.println("identifier : " + key);
		
		switch(key) {
			case "minedBlock" : verifyBlock(); break;// 1. ���� 2. ������������� 3. �ٸ� Peer ������� Ȯ�� 4. ������ ������ �����Ϸ�.
			case "verifyResult" : getVerifyResult(); break;
			default : break;
		}
	}
	
	private void verifyBlock() {
		blockVerify.doVerify(jsonObject);
		String msg = jsonSend.jsonVerifiedResultMessage(blockVerify.isTmpBlockValid());
		broadCasting(msg);
		blockVerify.setTotal(peerList.getSize());
		blockVerify.addVerifyResult(true); // ä����Ͽ� ����
		blockVerify.handleVerifyResult(blockVerify.isTmpBlockValid());
	}
	
	private void getVerifyResult() {
		boolean verifyResult = jsonObject.getBoolean("verifyResult");
		blockVerify.setTotal(peerList.getSize());
		blockVerify.handleVerifyResult(verifyResult);
	}
	
	private void broadCasting(String msg) {
		serverListener.send(msg);
	}
}
