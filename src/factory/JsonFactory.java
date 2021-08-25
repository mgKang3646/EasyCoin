package factory;

import json.JsonSend;
import json.PeerThreadReceive;
import json.ServerThreadReceive;
import model.PeerThread;
import model.ServerListener;

public class JsonFactory {
	
	public JsonSend getJsonSend(PeerThread peerThread) {
		return new JsonSend(peerThread);
	}
	
	public JsonSend getJsonSend(ServerListener serverListener) {
		return new JsonSend(serverListener);
	}
	
	public PeerThreadReceive getPeerThreadReceive() {
		return new PeerThreadReceive();
	}
	
	public ServerThreadReceive getServerThreadReceive() {
		return new ServerThreadReceive();
	}

}
