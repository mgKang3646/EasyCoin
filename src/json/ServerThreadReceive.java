package json;

import java.io.BufferedReader;
import java.net.InetAddress;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import factory.SocketThreadFactory;
import model.Peer;
import model.PeerList;
import model.SocketThread;

public class ServerThreadReceive implements JsonReceive{
	private Peer peer;
	private PeerList peerList;
	private JsonObject jsonObject;
	private SocketThreadFactory socketThreadFactory;

	public ServerThreadReceive(Peer peer) {
		this.peer = peer;
		this.peerList = peer.getPeerList();
		socketThreadFactory = new SocketThreadFactory();	
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
	
	//관심사 분리 필요
	private void makePeerThread() throws Exception {	
		String localhost = jsonObject.getString("localhost");
		String userName = jsonObject.getString("userName");
		peerList.addNewPeer(localhost,userName);
		socketThreadFactory.makePeerThread(localhost, peer).run();
	}
	
}
