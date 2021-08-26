package model;

import json.JsonSend;

public class Refresh {
	private OtherPeer leaderPeer;
	private JsonSend jsonSend;
	
	public OtherPeer getLeaderPeer() {
		return leaderPeer;
	}
	
	public void generateLeader() {
		int max = BlockChain.blockList.getBlockNum();
		for(OtherPeer otherPeer : Peer.peerList.getPeerList()) {
			if( otherPeer.getBlockNum() > max) leaderPeer = otherPeer;
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
