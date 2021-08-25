package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import factory.JsonFactory;
import json.JsonReceive;


public class ServerThread extends Thread {

	private Socket socket;
	private P2PNet p2pNet;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private JsonFactory jsonFactory;
	private JsonReceive jsonReceive;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
		jsonFactory = new JsonFactory();
		p2pNet = new P2PNet();
		jsonReceive = jsonFactory.getServerThreadReceive();
		printWriter = p2pNet.getPrintWriter(socket);
		bufferedReader = p2pNet.getBufferedReader(socket);
	}
	
	public void run() {
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
	
	private void closeIO() throws IOException {
		socket.close();
		bufferedReader.close();
		printWriter.close();
	}
	
	public void send(String msg) {
		printWriter.println(msg);
	}		
}
