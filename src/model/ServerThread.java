package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import factory.IOFactory;
import factory.JsonFactory;
import factory.UtilFactory;
import json.JsonReceive;
import json.JsonSend;


public class ServerThread extends SocketThread {

	private Socket socket;
	private Peer peer;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private JsonFactory jsonFactory;
	private UtilFactory utilFactory;
	private IOFactory ioFactory;
	private JsonReceive jsonReceive;
	private JsonSend jsonSend;
	
	public ServerThread(Socket socket, Peer peer)throws IOException{
		this.socket = socket;
		this.peer = peer;
		ioFactory = new IOFactory();
		utilFactory = new UtilFactory();
		jsonFactory = new JsonFactory();
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		jsonSend = jsonFactory.getJsonSend();
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
