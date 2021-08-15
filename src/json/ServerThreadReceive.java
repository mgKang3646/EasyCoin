package json;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.json.Json;
import javax.json.JsonObject;
import factory.SocketThreadFactory;
import model.Peer;
import model.SocketThread;

public class ServerThreadReceive implements JsonReceive{
	private Peer peer;
	private SocketThreadFactory socketThreadFactory;

	public ServerThreadReceive(Peer peer) {
		this.peer = peer;
		socketThreadFactory = new SocketThreadFactory();	
	}
	
	public JsonObject getJsonObject(BufferedReader br) {
		return Json.createReader(br).readObject();
	}

	public void processJsonQuery(JsonObject object) {
		try {
			String key = object.getString("identifier");
			switch(key) {
				case "connect" : makePeerThread(object); break;
				default : break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void makePeerThread(JsonObject object) throws Exception {	
		String localhost = object.getString("localhost");
		String[] address = localhost.split(":");
		Socket newSocket = new Socket(InetAddress.getByName(address[0]),Integer.valueOf(address[1])); // 소켓 생성과 동시에 연결 진행
		SocketThread socketThread = socketThreadFactory.getPeerThread(newSocket, peer);
		socketThread.start();
	}
	
}
