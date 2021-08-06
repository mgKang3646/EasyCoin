package factory;

import java.io.IOException;
import java.net.Socket;

import model.Peer;
import model.PeerThread;
import model.ServerThread;

public class SocketThreadFactory {
	
	public ServerThread getServerThread(Socket socket, Peer peer) throws IOException {
		return new ServerThread(socket,peer);
	}
	
	public PeerThread getPeerThread(Socket socket, Peer peer) throws IOException {
		return new PeerThread(socket, peer);
	}

}
