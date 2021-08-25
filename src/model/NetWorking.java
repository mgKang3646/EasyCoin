package model;

import java.util.ArrayList;
import database.Dao;

public class NetWorking {
	
	private Dao dao;
	private P2PNet p2pNet;
	
	public NetWorking() {
		p2pNet = new P2PNet();
		dao = new Dao();
	}
	
	public boolean runServerListener() {
		return p2pNet.runServerListener();
	}
	
	public boolean doConnect(OtherPeer otherPeer) {
		PeerThread peerThread = p2pNet.connectOtherPeer(otherPeer.getLocalhost());
		if(peerThread != null) {
			peerThread.start();
			p2pNet.requestConnect(peerThread);
			otherPeer.setPeerThread(peerThread);
			Peer.peerList.add(otherPeer);
			return true;
		}
		else return false;
	}
	
	public ArrayList<OtherPeer> getOtherPeers() {
		 return dao.getPeers(Peer.myPeer.getUserName());
	}
}
