package json;

import model.Block;
import model.PeerThread;
import model.ServerListener;

public class JsonSend {
	
	private JsonMessage jsonMessage;
	private PeerThread peerThread;
	private ServerListener serverListener;
	
	public JsonSend(PeerThread peerThread) {
		this.jsonMessage = new JsonMessage();
		this.peerThread = peerThread;
	}
	
	public JsonSend(ServerListener serverListener) {
		this.jsonMessage = new JsonMessage();
		this.serverListener = serverListener;
	}
	
	public void sendConnectMessage(String localhost, String userName) {
		String msg = jsonMessage.jsonConnectMessage(localhost, userName);
		peerThread.send(msg);
	}
	
	public void sendBlockMinedMessage(Block block) {
		String msg = jsonMessage.jsonBlockMinedMessage(block);
		serverListener.send(msg);
	}
	
	public void sendVerifiedResultMessage(boolean result) {
		String msg = jsonMessage.jsonVerifiedResultMessage(result);
		serverListener.send(msg);
	}

}
