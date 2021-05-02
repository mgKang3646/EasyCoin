package model;

import java.security.PublicKey;

public class TransactionOutput {
	
	public String id;
	public String miner;
	public PublicKey recipient;
	public float value;
	
	public TransactionOutput(PublicKey recipient, float value) {
		this.recipient = recipient;
		this.value = value;
	}
	
	public boolean isMine(PublicKey publickey) {
		return (this.recipient == publickey);
	}

	public String getId() {
		return id;
	}

	public String getMiner() {
		return miner;
	}

	public PublicKey getRecipient() {
		return recipient;
	}

	public float getValue() {
		return value;
	}

	public void setId(String id) {
		this.id = id;
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
