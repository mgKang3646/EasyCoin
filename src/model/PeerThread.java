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

public class PeerThread extends Thread {

	private Socket socket = null;
	private Peer peer;
	private JsonSend jsonSend;
	private JsonReceive jsonReceive;
	private PrintWriter printWriter;
	private BufferedReader br;
	private BufferedWriter bw;

	public PeerThread(Socket socket) throws IOException{
		this.socket = socket;
		initializeObjects();
	}
	
	public void initializeObjects() throws IOException {
		UtilFactory utilFactory = new UtilFactory();
		IOFactory ioFactory = new IOFactory();
		
		jsonSend = utilFactory.getJsonSend();
		jsonReceive = utilFactory.getJsonReceive();
		printWriter = ioFactory.getPrintWriter(socket);
		br = ioFactory.getBufferedReader(socket);
	}
	
	public void run()  {
		while(true) {
			JsonObject jsonObject = Json.createReader(br).readObject();
		}
	}
	
	public void send(String msg) throws IOException {
		printWriter.println(msg);
	}
	
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
}
