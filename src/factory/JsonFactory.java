package factory;

import json.JsonSend;
import json.PeerThreadReceive;
import json.ServerThreadReceive;
import model.Peer;
import model.ServerListener;
import model.SocketThread;

public class JsonFactory {
	
	public JsonSend getJsonSend(SocketThread socketThread) {
		return new JsonSend(socketThread);
	}
	
	public JsonSend getJsonSend(ServerListener serverListener) {
		return new JsonSend(serverListener);
	}
	
	public PeerThreadReceive getPeerThreadReceive(Peer peer) {
		return new PeerThreadReceive(peer);
	}
	
	public ServerThreadReceive getServerThreadReceive(Peer peer) {
		return new ServerThreadReceive(peer);
	}

}
