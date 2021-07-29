package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

public class PeerThread extends Thread {

	private Socket socket = null;
	private Peer peer;

	public PeerThread(Socket socket) throws IOException{
		this.socket = socket;
	}
	
	public void run() {
		
	}
	
	public void send(String msg) throws IOException {
		
	}
	
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
		
}
