package util;

import java.io.BufferedReader;
import java.net.InetAddress;
import java.net.Socket;
import javax.json.Json;
import javax.json.JsonObject;
import factory.SocketThreadFactory;
import model.Peer;
import model.SocketThread;

public class JsonReceive {
	
	private Peer peer;
	private SocketThreadFactory socketThreadFactory;

	public JsonReceive(Peer peer) {
		this.peer = peer;
		//initializeObjects();
	}
	
	public void initializeObjects() {
		socketThreadFactory = new SocketThreadFactory();
	}

	public JsonObject getJsonObject(BufferedReader br) {
		return Json.createReader(br).readObject();
	}

	public void processJsonQuery(JsonObject obj) throws Exception {
		String key = obj.getString("order");
		
		switch(key) {
			case "requestPeerThread" : makePeerThread(obj.getString("localhost")); break;
			default : break;
		}
	}
	
	private void makePeerThread(String localhost) throws Exception {		
		String[] address = localhost.split(":");
		Socket newSocket = new Socket(InetAddress.getByName(address[0]),Integer.valueOf(address[1])); // 소켓 생성과 동시에 연결 진행
		SocketThread socketThread = socketThreadFactory.getPeerThread(newSocket, peer);
		socketThread.start();
	}

}
