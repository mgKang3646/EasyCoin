package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import factory.SocketThreadFactory;

public class ServerListener extends Thread {

	private ServerSocket serverSocket;
	private Set<SocketThread> socketThreads;
	private SocketThreadFactory socketThreadFactory;
	private Peer peer;
	
	public ServerListener(String port) throws IOException{
		this.serverSocket = new ServerSocket(Integer.valueOf(port));
		initializeObjects();
	}
	
	private void initializeObjects() {
		socketThreadFactory = new SocketThreadFactory();
		socketThreads = new HashSet<SocketThread>();
		
	}
	
	public void run() {
		try {
			while(true) {
				SocketThread socketThread = socketThreadFactory.getServerThread(serverSocket.accept(),peer);
				socketThread.start();
				socketThreads.add(socketThread);
			}
		} catch (Exception e) {
		}
	}
	
	public void send(String msg) {
		socketThreads.forEach(socketThread -> socketThread.send(msg)) ;
	}
	
	@Override
	public String toString() {
		return "localhost:"+this.serverSocket.getLocalPort();
	}
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	
}
