package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import factory.JsonFactory;
import json.JsonReceive;

public class PeerThread extends Thread {

	private Socket socket;
	private JsonFactory jsonFactory;
	private P2PNet p2pNet;
	private JsonReceive jsonReceive;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;

	public PeerThread(Socket socket) {
		this.socket = socket;
		this.p2pNet = new P2PNet();		
		jsonFactory = new JsonFactory();
		jsonReceive = jsonFactory.getPeerThreadReceive();
		printWriter = p2pNet.getPrintWriter(socket);
		bufferedReader = p2pNet.getBufferedReader(socket);
	}
	
	public void run()  {
		try {
			while(true) {
				jsonReceive.read(bufferedReader);
			}
		}catch(Exception e) {
			try {
				e.printStackTrace();
				closeIO();
			} catch (IOException e1) {}
		}
	}
	
	public void closeIO() throws IOException {
		socket.close();
		bufferedReader.close();
		printWriter.close();
	}
	
	public void send(String msg){
		printWriter.println(msg);
	}
}
