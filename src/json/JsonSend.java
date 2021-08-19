package json;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import model.Block;
import model.ServerListener;
import model.SocketThread;

public class JsonSend {
	
	private JsonMessage jsonMessage;
	private SocketThread socketThread;
	private ServerListener serverListener;
	
	public JsonSend(SocketThread socketThread) {
		this.jsonMessage = new JsonMessage();
		this.socketThread = socketThread;
	}
	
	public JsonSend(ServerListener serverListener) {
		this.jsonMessage = new JsonMessage();
		this.serverListener = serverListener;
	}
	
	public void sendConnectMessage(String localhost, String userName) {
		String msg = jsonMessage.jsonConnectMessage(localhost, userName);
		socketThread.send(msg);
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
