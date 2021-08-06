package util;

import java.io.BufferedReader;
import java.net.InetAddress;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import model.Peer;
import model.PeerThread;

public class JsonReceive {
	
	private Peer peer;
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	public JsonObject getJsonObject(BufferedReader br) {
		return Json.createReader(br).readObject();
	}

	public void processJsonQuery(JsonObject obj) throws Exception {
		String key = obj.getString("order");
		
		switch(key) {
			case "makePeerThread" : makePeerThread(obj.getString("localhost")); break;
			default : break;
		}
	}
	
	private void makePeerThread(String localhost) throws Exception {
		
		String[] address = localhost.split(":");
		Socket newSocket = new Socket(InetAddress.getByName(address[0]),Integer.valueOf(address[1])); // 소켓 생성과 동시에 연결 진행
		PeerThread peerThread = new PeerThread(newSocket);
		peerThread.setPeer(peer);
		peerThread.start();
		
	}

}
