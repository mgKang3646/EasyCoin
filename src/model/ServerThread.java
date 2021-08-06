package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import factory.IOFactory;
import factory.UtilFactory;
import util.JsonReceive;
import util.JsonSend;

public class ServerThread extends Thread {

	private Socket socket;
	private Peer peer;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private JsonSend jsonSend;
	private JsonReceive jsonReceive;
	
	public ServerThread(Socket socket)throws IOException{
		this.socket = socket;
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		IOFactory ioFactory = new IOFactory();
		UtilFactory utilFactory = new UtilFactory();
		
		jsonSend = utilFactory.getJsonSend();
		jsonReceive = utilFactory.getJsonReceive();
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
					closeIO();
				} catch (IOException e1) {}
		}
	}
	
	public void closeIO() throws IOException {
		socket.close();
		bufferedReader.close();
		printWriter.close();
	}
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	
}
