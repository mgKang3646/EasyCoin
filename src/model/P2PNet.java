package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import json.JsonSend;

public class P2PNet {
	
	private static ServerListener serverListener;
	private P2PSocket p2pSocket;
	private JsonSend jsonSend;
	
	public P2PNet() {
		p2pSocket = new P2PSocket();
	}

	public void makeServerListener() {
		ServerSocket serverSocket = p2pSocket.getServerSocket(Peer.myPeer.getLocalhost());
		if(serverSocket != null) {
			serverListener = new ServerListener(serverSocket);
		}	
	}
	
	public PeerThread makePeerThread(String localhost) {
		Socket socket = p2pSocket.getNewSocket(localhost);
		if(socket != null) return new PeerThread(socket);
		else return null;
	}
	
	public void requestConnect(PeerThread peerThread) {
		jsonSend = new JsonSend(peerThread);
		jsonSend.sendConnectMessage(Peer.myPeer.getLocalhost(),Peer.myPeer.getUserName());
	}
	
	public static ServerListener getServerListener() {
		return serverListener;
	}
	
	public void runServerListener() {
		serverListener.start();
	}
	
	public BufferedReader getBufferedReader(Socket socket)  {
		try {
			return new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public PrintWriter getPrintWriter(Socket socket)  {
		try {
			return new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
