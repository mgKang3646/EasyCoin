package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class P2PSocket {
	
	private int portNum;
	private InetAddress ip;
	
	public ServerSocket getServerSocket(String localhost) {
		try {
			doSplit(localhost);
			return new ServerSocket(portNum);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Socket getNewSocket(String localhost) {
		try {
			doSplit(localhost);
			Socket newSocket = new Socket(ip,portNum);
			return newSocket;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void doSplit(String localhost) {
		try {
			String[] address = localhost.split(":");
			ip = InetAddress.getByName(address[0]);
			portNum = Integer.valueOf(address[1]);
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public Socket doConnect(String localhost) {
		Socket socket = new Socket();
		if(connectSocketAddress(getSocketAddress(localhost),socket)) return socket;
		else return null;
	}
	
	private SocketAddress getSocketAddress(String localhost)  {
		doSplit(localhost);
		InetSocketAddress socketAddress = new InetSocketAddress(ip,portNum);
		return socketAddress;
	}
	
	private boolean connectSocketAddress(SocketAddress socketAddress, Socket socket){
		try {
			socket.connect(socketAddress);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
