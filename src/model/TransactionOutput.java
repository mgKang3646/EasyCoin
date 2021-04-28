package model;

import java.security.PublicKey;

public class TransactionOutput {
	
	public String id;
	public PublicKey recipient;
	public float value;
	private String parentTransactionId;
	
	public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
		this.recipient = recipient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
	}
	
	public boolean isMine(PublicKey publickey) {
		return (this.recipient == publickey);
	}

}
