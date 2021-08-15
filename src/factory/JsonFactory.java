package factory;

import json.JsonSend;
import json.PeerThreadReceive;
import json.ServerThreadReceive;
import model.Peer;

public class JsonFactory {
	
	public JsonSend getJsonSend() {
		return new JsonSend();
	}
	
	public PeerThreadReceive getPeerThreadReceive(Peer peer) {
		return new PeerThreadReceive(peer);
	}
	
	public ServerThreadReceive getServerThreadReceive(Peer peer) {
		return new ServerThreadReceive(peer);
	}

}
