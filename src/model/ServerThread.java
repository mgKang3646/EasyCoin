package model;

import java.net.Socket;
import java.net.UnknownHostException;


public class ServerThread extends Thread {

	private Socket socket;
	private Peer peer;
	
	public ServerThread(Socket socket)throws UnknownHostException{
		this.socket = socket;
	}
	
	public void run() {
		System.out.println(socket.getLocalAddress()+"���������� ����");
	}
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
}
