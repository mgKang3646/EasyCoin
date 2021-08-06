package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import factory.IOFactory;
import factory.UtilFactory;
import util.JsonReceive;
import util.JsonSend;

public class PeerThread extends SocketThread {

	private Socket socket = null;
	private Peer peer;
	private JsonSend jsonSend;
	private JsonReceive jsonReceive;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;

	public PeerThread(Socket socket, Peer peer) throws IOException{
		this.socket = socket;
		this.peer = peer;
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		UtilFactory utilFactory = new UtilFactory();
		IOFactory ioFactory = new IOFactory();
		
		jsonSend = utilFactory.getJsonSend();
		jsonReceive = utilFactory.getJsonReceive(peer);
		printWriter = ioFactory.getPrintWriter(socket);
		bufferedReader = ioFactory.getBufferedReader(socket);
	}
	@Override
	public void run()  {
		try {
			while(true) {
				jsonReceive.getJsonObject(bufferedReader);
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
	
	public void send(String msg) throws IOException {
		printWriter.println(msg);
	}
	
}
