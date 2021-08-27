package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import json.PeerThreadReceive;
import util.P2PNet;

public class PeerThread extends Thread {

	private Socket socket;
	private P2PNet p2pNet;
	private PeerThreadReceive peerThreadReceive;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;

	public PeerThread(Socket socket) {
		this.socket = socket;
		this.p2pNet = new P2PNet();	
		peerThreadReceive = new PeerThreadReceive();
		printWriter = p2pNet.getPrintWriter(socket);
		bufferedReader = p2pNet.getBufferedReader(socket);
	}
	
	public void run()  {
		try {
			while(true) {
				peerThreadReceive.setPeerThread(this);
				peerThreadReceive.read(bufferedReader);
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
