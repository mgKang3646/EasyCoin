package model;

import java.io.IOException;
import java.net.Socket;

public class PeerThread extends Thread {

	private Socket socket = null;

	public PeerThread(Socket socket) throws IOException{
		this.socket = socket;
	}
	
	public void run() {
		System.out.println(socket.getLocalAddress() +"Peer스레드 실행");
	}
		
}
