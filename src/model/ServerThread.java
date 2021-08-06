package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import factory.IOFactory;

public class ServerThread extends Thread {

	private Socket socket;
	private Peer peer;
	private BufferedReader bufferedReader;
	
	public ServerThread(Socket socket)throws IOException{
		this.socket = socket;
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		IOFactory ioFactory = new IOFactory();
		this.bufferedReader = ioFactory.getBufferedReader(socket);

	}
	
	public void run() {
		try {
			while(true) {
					JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
					
					if(jsonObject.containsKey("localhost")) {
						String hostAddress = jsonObject.getString("localhost");
						String[] address = hostAddress.split(":");
						
						Socket newSocket = new Socket(InetAddress.getByName(address[0]),Integer.valueOf(address[1])); // 소켓 생성과 동시에 연결 진행
						PeerThread peerThread = new PeerThread(newSocket);
						peerThread.setPeer(peer);
						peerThread.start();
					}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
}
