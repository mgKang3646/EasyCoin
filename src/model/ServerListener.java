package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerListener extends Thread {

	private ServerSocket serverSocket;
	private Set<ServerThread> serverThreads = new HashSet<ServerThread>();
	
	public ServerListener(String port) throws IOException{
		this.serverSocket = new ServerSocket(Integer.valueOf(port));	
		}
	
	@SuppressWarnings("deprecation")
	public void run() {
		try {
			while(true) {
				ServerThread serverThread = new ServerThread(serverSocket.accept());
				serverThreads.add(serverThread);
				serverThread.start();
			}
		} catch (Exception e) {
			serverThreads.forEach(t->t.stop());
		}
	}
}
