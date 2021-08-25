package model;

import java.security.PublicKey;

public class OtherPeer {
	private String userName;
	private String localhost;
	private PublicKey publicKey;
	private boolean isLeader;
	private PeerThread peerThread;
	
	public String getUserName() {
		return userName;
	}
	public String getLocalhost() {
		return localhost;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public boolean isLeader() {
		return isLeader;
	}
	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	public PeerThread getPeerThread() {
		return peerThread;
	}
	public void setPeerThread(PeerThread peerThread) {
		this.peerThread = peerThread;
	}
	
}
