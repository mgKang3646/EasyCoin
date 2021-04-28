package model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

public class WalletModel {
	
	private String userLocalHost;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private String username;
	private HashMap<String,TransactionOutput> UTXO_Wallet = new HashMap<String,TransactionOutput>();

	public HashMap<String, TransactionOutput> getUTXO_Wallet() {
		return UTXO_Wallet;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public String getUserLocalHost() {
		return userLocalHost;
	}

	public void setUserLocalHost(String userLocalHost) {
		this.userLocalHost = userLocalHost;
	}
}
