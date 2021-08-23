package model;

import java.util.ArrayList;

public class PeerList {
	private ArrayList<OtherPeer> peerList;
	
	public PeerList() {
		peerList = new ArrayList<OtherPeer>();
	}
	
	public ArrayList<OtherPeer> getPeerList(){
		return peerList;
	}
	
	public void add(OtherPeer otherPeer) {
		peerList.add(otherPeer);
	}
	
	public void addNewPeer(String localhost, String userName) {
		OtherPeer newPeer = new OtherPeer();
		newPeer.setLocalhost(localhost);
		newPeer.setUserName(userName);
		add(newPeer);
	}
	
	public int getSize() {
		return peerList.size();
	}

}
