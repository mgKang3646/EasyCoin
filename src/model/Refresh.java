package model;

import json.JsonSend;
import util.P2PNet;

public class Refresh {
	private OtherPeer leaderPeer;
	private JsonSend jsonSend;
	
	public OtherPeer getLeaderPeer() {
		return leaderPeer;
	}
	
	public void generateLeader() {
		int max = BlockChain.blockList.getBlockNum();
		System.out.println("³»²¨ : " + max);
		for(OtherPeer otherPeer : Peer.peerList.getPeerList()) {
			if( otherPeer.getBlockNum() > max) leaderPeer = otherPeer;
			System.out.println(otherPeer.getUserName() + " : " + otherPeer.getBlockNum());
		}
		if(leaderPeer == null) Peer.myPeer.setLeader(true);
		else leaderPeer.setLeader(true);
	}
	
	public void requestBlockNum() {
		jsonSend = new JsonSend(P2PNet.getServerListener());
		jsonSend.sendRequestBlockNum();
	}
	
	public void requestLeaderBlocks() {
		jsonSend = new JsonSend(leaderPeer.getPeerThread());
		jsonSend.sendRequestLeaderBlocksMessage();
	}
}
