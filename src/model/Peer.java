package model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Peer {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private ServerListener serverListener;
	private String userName;
	private String localhost;
	private BlockChain blockchain;
	private PeerList peerList;
	
	public Peer() {
		this.blockchain = new BlockChain(); 
		this.peerList = new PeerList();
	}

	public BlockChain getBlockchain() {
		return blockchain;
	}
	public PeerList getPeerList() {
		return peerList;
	}
	public String getUserName() {
		return userName;
	}
	public String getLocalhost() {
		return localhost;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public ServerListener getServerListener() {
		return serverListener;
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
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public void setServerListener(ServerListener serverListener) {
		this.serverListener = serverListener;
	}
	
}
