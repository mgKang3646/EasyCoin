package model;

import java.util.ArrayList;

public class PeerList {
	private ArrayList<Peer> peerList;
	
	public PeerList() {
		peerList = new ArrayList<Peer>();
	}
	
	public ArrayList<Peer> getPeerList(){
		return peerList;
	}
	
	public void addPeer(Peer peer) {
		peerList.add(peer);
	}
	
	public void addNewPeer(String localhost, String userName) {
		Peer newPeer = new Peer();
		newPeer.setLocalhost(localhost);
		newPeer.setUserName(userName);
		addPeer(newPeer);
	}
	
	public int getSize() {
		return peerList.size();
	}

}
