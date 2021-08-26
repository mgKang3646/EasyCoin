package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import json.ServerThreadReceive;


public class ServerThread extends Thread {

	private Socket socket;
	private P2PNet p2pNet;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private ServerThreadReceive serverThreadReceive;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
		p2pNet = new P2PNet();
		serverThreadReceive = new ServerThreadReceive();
		printWriter = p2pNet.getPrintWriter(socket);
		bufferedReader = p2pNet.getBufferedReader(socket);
	}
	
	public void run() {
		try {
			while(true) {
				serverThreadReceive.read(bufferedReader);
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
