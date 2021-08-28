package json;

import java.security.PublicKey;

import model.Block;
import model.PeerThread;
import model.ServerListener;
import model.TransactionInput;

public class JsonSend {
	
	private JsonMessage jsonMessage;
	private PeerThread peerThread; // 유니캐스팅 
	private ServerListener serverListener; // 브로드캐스팅
	
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
	
	public void sendRequestBlockNum() {
		String msg = jsonMessage.jsonRequestBlockNumMessage();
		serverListener.send(msg);
	}
	
	public void sendResponseBlockNum() {
		String msg = jsonMessage.jsonResPonseBlockNumMessage();
		peerThread.send(msg);
	}
	
	public void sendRequestLeaderBlocksMessage() {
		String msg = jsonMessage.jsonRequestLeaderBlockMessage();
		peerThread.send(msg);
	}
	
	public void sendResponseLeaderBlockMessage(Block block) {
		String msg = jsonMessage.jsonResponseLeaderBlockMessage(block);
		peerThread.send(msg);
	}
	
	public void sendRequestITXOMessage(PublicKey recipient) {
		String msg = jsonMessage.jsonRequestITXO(recipient);
		serverListener.send(msg);
	}
	
	public void sendResponseITXOMessage(TransactionInput itxo) {
		String msg = jsonMessage.jsonResponseITXO(itxo);
		peerThread.send(msg);
	}
}
