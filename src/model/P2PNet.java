package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import factory.JsonFactory;
import json.JsonSend;

public class P2PNet {
	
	private static ServerListener serverListener;
	private P2PSocket p2pSocket;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	
	public P2PNet() {
		p2pSocket = new P2PSocket();
		jsonFactory = new JsonFactory();
	}
	
	public static ServerListener getServerListener() {
		return serverListener;
	}
	
	public boolean runServerListener() {
		ServerSocket serverSocket = p2pSocket.getServerSocket(Peer.myPeer.getLocalhost());
		if(serverSocket != null) {
			serverListener = new ServerListener(serverSocket);
			serverListener.start();
			return true;
		}else return false;		
	}
	
	public PeerThread connectOtherPeer(String localhost) {
		Socket socket = p2pSocket.doConnect(localhost);
		if(socket != null) {
			return new PeerThread(socket);
		}else return null;
	}
	
	public PeerThread makePeerThread(String localhost) {
		return new PeerThread(p2pSocket.getNewSocket(localhost));
	}
	
	public void requestConnect(PeerThread peerThread) {
		jsonSend = jsonFactory.getJsonSend(peerThread);
		jsonSend.sendConnectMessage(Peer.myPeer.getLocalhost(),Peer.myPeer.getUserName());
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
