package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import util.JsonUtil;

public class PeerThread extends Thread {

	private Socket socket = null;
	private Peer peer;
	private JsonUtil jsonUtil;
	private PrintWriter printWriter;
	private BufferedReader br;
	private BufferedWriter bw;

	public PeerThread(Socket socket) throws IOException{
		this.socket = socket;
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		jsonUtil = new JsonUtil();
		printWriter = new PrintWriter(socket.getOutputStream(),true);
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void run()  {
		while(true) {
			System.out.println("Peer스레드 실행 : localhost:"+socket.getLocalPort());
			JsonObject jsonObject = Json.createReader(br).readObject();
		}
	}
	
	public void send(StringWriter msg) throws IOException {
		printWriter.println(msg);
	}
	
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
}
