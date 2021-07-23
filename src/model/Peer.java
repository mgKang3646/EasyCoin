package model;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Peer {
	PrivateKey privateKey;
	PublicKey publicKey;
	String userName;
	String localhost;
	
	public Peer(PrivateKey privateKey, PublicKey publicKey, String userName, String localhost) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.userName = userName;
		this.localhost = localhost;
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
	
	
}
