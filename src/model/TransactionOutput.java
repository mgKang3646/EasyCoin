package model;

import java.security.PublicKey;

public class TransactionOutput {
	
	public String id;
	public PublicKey recipient;
	public float value;
	
	public TransactionOutput(PublicKey recipient, float value) {
		this.recipient = recipient;
		this.value = value;
	}
	
	public boolean isMine(PublicKey publickey) {
		return (this.recipient == publickey);
	}

}
