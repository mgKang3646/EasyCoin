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

public class PeerThread extends SocketThread {

	private Socket socket = null;
	private Peer peer;
	private JsonFactory jsonFactory;
	private UtilFactory utilFactory;
	private IOFactory ioFactory;
	private JsonSend jsonSend;
	private JsonReceive jsonReceive;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;

	public PeerThread(Socket socket, Peer peer) throws IOException{
		this.socket = socket;
		this.peer = peer;
		utilFactory = new UtilFactory();
		ioFactory = new IOFactory();
		jsonFactory = new JsonFactory();
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		jsonSend = jsonFactory.getJsonSend();
		jsonReceive = jsonFactory.getPeerThreadReceive(peer);
		printWriter = ioFactory.getPrintWriter(socket);
		bufferedReader = ioFactory.getBufferedReader(socket);
	}
	@Override
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
