package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import factory.IOFactory;
import factory.UtilFactory;
import util.JsonUtil;

public class PeerThread extends Thread {

	private Socket socket = null;
	private Peer peer;
	private JsonUtil jsonUtil;
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
		
		jsonUtil = utilFactory.getJsonUtil();
		printWriter = ioFactory.getPrintWriter(socket);
		br = ioFactory.getBufferedReader(socket);
	}
	
	public void run()  {
		while(true) {
			JsonObject jsonObject = Json.createReader(br).readObject();
		}
	}
	
	public void send(StringWriter msg) throws IOException {
		printWriter.println(msg);
	}
	
	
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
}
