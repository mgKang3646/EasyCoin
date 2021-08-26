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
		p2pNet.makeServerListener();
		if(P2PNet.getServerListener() == null) return false;
		else {
			p2pNet.runServerListener();
			return true;
		}
	}
	
	public boolean doConnect(OtherPeer otherPeer) {
		PeerThread peerThread = p2pNet.makePeerThread(otherPeer.getLocalhost());
		if(peerThread != null) {
			peerThread.start();
			otherPeer.setPeerThread(peerThread);
			p2pNet.requestConnect(peerThread);
			Peer.peerList.add(otherPeer);
			return true;
		}
		else return false;
	}
	
	public ArrayList<OtherPeer> getOtherPeers() {
		 return dao.getPeers(Peer.myPeer.getUserName());
	}
}
