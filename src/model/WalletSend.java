package model;

import json.JsonSend;
import util.P2PNet;

public class WalletSend {

	private JsonSend jsonSend;
	
	public void requestITXO() {
		jsonSend = new JsonSend(P2PNet.getServerListener());
		jsonSend.sendRequestITXOMessage(Peer.myPeer.getPublicKey());
	}
	
	public void requestDeleteUTXO(TransactionInput itxo) {
		jsonSend = new JsonSend(Peer.peerList.searchOtherPeer(itxo.getMiner()).getPeerThread()); // ä���ڰ� ���ӻ��¿��߸� ����
		jsonSend.sendDeleteUTXO(itxo);
	}
}
