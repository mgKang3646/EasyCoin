package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerListener extends Thread {

	private String port;
	private ServerSocket serverSocket;
	private String hostAddress = null;
	private Set<ServerThread> serverThreads = new HashSet<ServerThread>();
	private PeerModel peerModel;
	
	public ServerListener(String port,PeerModel peerModel) throws IOException{
		this.peerModel = peerModel;
		this.port = port;
		this.serverSocket = new ServerSocket(Integer.valueOf(port));
		this.hostAddress = InetAddress.getLocalHost().getHostAddress(); 
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		try {
			while(true) {
				ServerThread serverThread = new ServerThread(serverSocket.accept(),this,peerModel);
				serverThreads.add(serverThread);
				serverThread.start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serverThreads.forEach(t->t.stop());
		}
	}
	public void sendMessage(String message) {
		try {
			serverThreads.forEach(t->t.getPrintWriter().println(message));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public String getPort() { return this.port;}
	public Set<ServerThread> getServerThreadThreads() { return serverThreads;}
	public String toString() { return hostAddress+":"+port; }
	
}
