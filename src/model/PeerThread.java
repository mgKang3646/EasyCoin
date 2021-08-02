package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import util.JsonUtil;

public class PeerThread extends Thread {

	private Socket socket = null;
	private Peer peer;
	private JsonUtil jsonUtil;
	private PrintWriter printWriter;

	public PeerThread(Socket socket) throws IOException{
		this.socket = socket;
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		jsonUtil = new JsonUtil();
		printWriter = new PrintWriter(socket.getOutputStream(),true);
	}
	
	public void run()  {
		System.out.println("Peer스레드 생성완료 실행!");
	}
	
	public void send(StringWriter msg) throws IOException {
		printWriter.println(msg);
	}
	
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
}
