package util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocketUtil {
	
	Socket socket;
	
	public SocketUtil() {
		this.socket = new Socket();
	}
	
	public SocketAddress makeSocketAddress(String localhost) throws UnknownHostException {
		
		String[] address = localhost.split(":");
		InetAddress ip = InetAddress.getByName(address[0]);
		int port = Integer.valueOf(address[1]);
		InetSocketAddress socketAddress = new InetSocketAddress(ip,port);
		
		return socketAddress;
	}
	
	public boolean connectToSocketAddress(SocketAddress socketAddress) {
		try {
			socket.connect(socketAddress);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void makeNewSocket() {
		this.socket = new Socket();
	}
	
	public Socket getSocket() {
		return this.socket;
	}

}
