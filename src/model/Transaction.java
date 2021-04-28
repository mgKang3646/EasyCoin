package model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


public class Transaction {
	
	public PublicKey sender;
	public PublicKey recipient;
	public float value;
	public byte[] signature;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	
	public Transaction(PublicKey sender, PublicKey recipient, float value) {
		this.sender = sender ;
		this.recipient = recipient;
		this.value = value;
	}
	
	// 서명검증
	public boolean processTransaction() {
			return false;
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = BlockUtil.getStringFromKey(sender)+BlockUtil.getStringFromKey(recipient)+Float.toString(value);
		signature = BlockUtil.applyECDSASig(privateKey, data);// 거래 data를 통해 시그니처가 생성되기 떄문에 data가 바뀌면 시그니쳐가 달라진다. 
	}
	
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	public boolean verifySignature() {
		String data = BlockUtil.getStringFromKey(sender)+BlockUtil.getStringFromKey(recipient)+Float.toString(value);
		return BlockUtil.verifyECDSASig(sender, data, signature);
	}

}
