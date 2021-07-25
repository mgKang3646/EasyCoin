package model;

import java.net.Socket;
import java.net.UnknownHostException;


public class ServerThread extends Thread {

	private Socket socket;
	
	public ServerThread(Socket socket)throws UnknownHostException{
		this.socket = socket;
	}
	
	public void run() {
		System.out.println(socket.getLocalAddress()+"서버스레드 생성");
	}
}
