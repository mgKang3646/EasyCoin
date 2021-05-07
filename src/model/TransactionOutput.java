package model;

import java.security.PublicKey;

import org.apache.commons.codec.digest.DigestUtils;

public class TransactionOutput {
	
	String miner;
	PublicKey recipient;
	float value;
	String txoHash;
	double nonce;
	String transactionHash;
	
	
	public String getTransactionHash() {
		return transactionHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}

	public TransactionOutput(PublicKey recipient, float value, String miner) {
		this.recipient = recipient;
		this.value = value;
		this.miner = miner;
	}
	
	public void generateHash() {
		this.nonce = Math.random();	
		this.txoHash = DigestUtils.sha256Hex(miner+recipient+value+nonce);
	}
	public void setHash(String hash) {
		this.txoHash = hash;
	}
	
	public double getNonce() {
		return nonce;
	}
	public void setNonce(double nonce) {
		this.nonce = nonce;
	}

	public boolean isMine(PublicKey publickey) {
		return this.recipient.toString().equals(publickey.toString());
	}

	public String getMiner() {
		return miner;
	}

	public String getTxoHash() {
		return txoHash;
	}

	public PublicKey getRecipient() {
		return recipient;
	}

	public float getValue() {
		return value;
	}

	public void setMiner(String miner) {
		this.miner = miner;
	}

	public void setRecipient(PublicKey recipient) {
		this.recipient = recipient;
	}

	public void setValue(float value) {
		this.value = value;
	}



}
