package model;

import json.JsonSend;
import util.P2PNet;

public class Wallet {
	public static UTXO utxo = new UTXO();
	public static ITXO itxo = new ITXO();
	private JsonSend jsonSend;
	
	public float getBalance() {
		return itxo.getBalance();
	}
	
	public void requestITXO() {
		jsonSend = new JsonSend(P2PNet.getServerListener());
		jsonSend.sendRequestITXOMessage(Peer.myPeer.getPublicKey());
	}
	
}
