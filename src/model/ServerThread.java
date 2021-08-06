package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import factory.IOFactory;
import factory.UtilFactory;
import util.JsonReceive;
import util.JsonSend;

public class ServerThread extends SocketThread {

	private Socket socket;
	private Peer peer;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private JsonSend jsonSend;
	private JsonReceive jsonReceive;
	
	public ServerThread(Socket socket, Peer peer)throws IOException{
		this.socket = socket;
		this.peer = peer;
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		IOFactory ioFactory = new IOFactory();
		UtilFactory utilFactory = new UtilFactory();
		
		jsonSend = utilFactory.getJsonSend();
		jsonReceive = utilFactory.getJsonReceive(peer);
		printWriter = ioFactory.getPrintWriter(socket);
		bufferedReader = ioFactory.getBufferedReader(socket);
	}
	
	public void run() {
		try {
			while(true) {
					jsonReceive.processJsonQuery(jsonReceive.getJsonObject(bufferedReader));
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
