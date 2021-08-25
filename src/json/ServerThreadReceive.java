package json;

import java.io.BufferedReader;

import javax.json.Json;
import javax.json.JsonObject;

import model.P2PNet;
import model.Peer;

public class ServerThreadReceive implements JsonReceive{
	private JsonObject jsonObject;
	private P2PNet p2pNet;

	public ServerThreadReceive() {
		p2pNet = new P2PNet();
	}
	public void read(BufferedReader bufferedReader) {
		setJsonObject(bufferedReader);
		processJsonQuery();
	}
	
	private void setJsonObject(BufferedReader bufferedReader) {
		jsonObject = Json.createReader(bufferedReader).readObject();
	}

	private void processJsonQuery() {
		try {
			String key = jsonObject.getString("identifier");
			switch(key) {
				case "connect" : makePeerThread(); break;
				default : break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void makePeerThread() throws Exception {	
		String localhost = jsonObject.getString("localhost");
		String userName = jsonObject.getString("userName");
		Peer.peerList.addNewPeer(localhost, userName);
		p2pNet.makePeerThread(localhost).run();
	}
}
