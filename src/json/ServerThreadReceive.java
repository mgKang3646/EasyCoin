package json;

import java.io.BufferedReader;

import javax.json.Json;
import javax.json.JsonObject;

import model.Block;
import model.BlockChain;
import model.BlockMaker;
import model.OtherPeer;
import model.Peer;
import model.PeerThread;
import model.TransactionInput;
import model.Wallet;
import util.P2PNet;
import util.ThreadUtil;

public class ServerThreadReceive{
	private JsonObject jsonObject;
	private BlockMaker blockMaker;
	private JsonSend jsonSend;
	private P2PNet p2pNet;

	public ServerThreadReceive() {
		p2pNet = new P2PNet();
		blockMaker = new BlockMaker();
	}
	
	public void read(BufferedReader bufferedReader) {
		setJsonObject(bufferedReader);
		processJsonQuery();
	}
	
	private void setJsonObject(BufferedReader bufferedReader) {
		jsonObject = Json.createReader(bufferedReader).readObject();
	}

	private void processJsonQuery() {
		try {
			String key = jsonObject.getString("identifier");
			switch(key) {
				case "connect" : makePeerThread(); break;
				case "responseBlockNum" : aggregateBlockNum(); break;
				case "requestLeaderBlock" : reponseLeaderBlocks(); break;
				case "responseLeaderBlock" : getLeaderBlock(); break;
				case "responseITXO" : handleITXO(); break;
				case "deleteUTXO" : deleteUTXO(); break;
				default : break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void makePeerThread() {	
		String localhost = jsonObject.getString("localhost");
		String userName = jsonObject.getString("userName");
		OtherPeer otherPeer = new OtherPeer();
		PeerThread peerThread = p2pNet.makePeerThread(localhost);
		otherPeer.setLocalhost(localhost);
		otherPeer.setUserName(userName);
		otherPeer.setPeerThread(peerThread);
		
		Peer.peerList.add(otherPeer);
		peerThread.start();
	}
	
	private void aggregateBlockNum() {
		String userName = jsonObject.getString("userName");
		int blockNum = jsonObject.getInt("blockNum");
		Peer.peerList.searchOtherPeer(userName).setBlockNum(blockNum);
	}
	
	private void reponseLeaderBlocks() {
		String userName = jsonObject.getString("userName");
		PeerThread peerThread = Peer.peerList.searchOtherPeer(userName).getPeerThread();
		jsonSend = new JsonSend(peerThread);
		for(Block block : BlockChain.blockList.getBlocks()) {
			jsonSend.sendResponseLeaderBlockMessage(block);
			System.out.println("블럭 송신");
			ThreadUtil.sleepThread(300);
		}
	}
	
	private void getLeaderBlock() {
		if(jsonObject.getInt("blockNum") == 0) {
			BlockChain.blockList.resetBlocks();
			BlockChain.getBlocklist().applyBlock(blockMaker.makeLeaderBlock(jsonObject));
		}
		else BlockChain.getBlocklist().applyBlock(blockMaker.makeLeaderBlock(jsonObject));
	}
	
	private void handleITXO() {
		if(Wallet.itxo.verifyITXO(jsonObject)) {
			TransactionInput itxo = Wallet.itxo.makeITXO(jsonObject);
			Wallet.itxo.addITXO(itxo);
		}
	}
	
	private void deleteUTXO() {
		System.out.println("UTXO 제거");
		String utxoHash = jsonObject.getString("utxoHash");
		Wallet.utxo.deleteUTXO(utxoHash);
	}
}
