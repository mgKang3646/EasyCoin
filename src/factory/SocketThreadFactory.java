package factory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import model.Peer;
import model.PeerThread;
import model.ServerThread;
import model.SocketThread;

public class SocketThreadFactory {
	
	public ServerThread getServerThread(Socket socket, Peer peer) throws IOException {
		return new ServerThread(socket,peer);
	}
	
	public PeerThread getPeerThread(Socket socket, Peer peer) throws IOException {
		return new PeerThread(socket, peer);
	}
	
	public PeerThread makePeerThread(String localhost, Peer peer) {
		try {
			String[] address = localhost.split(":");
			Socket newSocket = new Socket(InetAddress.getByName(address[0]),Integer.valueOf(address[1]));
			PeerThread peerThread = getPeerThread(newSocket, peer);
			return peerThread;
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			return null;
		} 
	}
}
