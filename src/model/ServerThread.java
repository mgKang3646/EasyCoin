package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import factory.IOFactory;
import factory.JsonFactory;
import json.JsonReceive;


public class ServerThread extends SocketThread {

	private Socket socket;
	private Peer peer;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private JsonFactory jsonFactory;
	private IOFactory ioFactory;
	private JsonReceive jsonReceive;
	
	public ServerThread(Socket socket, Peer peer)throws IOException{
		this.socket = socket;
		this.peer = peer;
		ioFactory = new IOFactory();
		jsonFactory = new JsonFactory();
		jsonReceive = jsonFactory.getServerThreadReceive(peer);
		printWriter = ioFactory.getPrintWriter(socket);
		bufferedReader = ioFactory.getBufferedReader(socket);
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
