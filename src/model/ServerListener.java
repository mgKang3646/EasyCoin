package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerListener extends Thread {

	private ServerSocket serverSocket;
	private Set<ServerThread> serverThreads;
	
	public ServerListener(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		serverThreads = new HashSet<ServerThread>();	
	}
	
	public void run() {
		while(true) {
			try {
				ServerThread serverThread = new ServerThread(serverSocket.accept());
				serverThread.start();
				serverThreads.add(serverThread);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void send(String msg) {
		serverThreads.forEach(socketThread -> socketThread.send(msg)) ;
	}
	
	@Override
	public String toString() {
		return "localhost:"+this.serverSocket.getLocalPort();
	}
}
