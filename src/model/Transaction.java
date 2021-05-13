package model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;


public class Transaction {// => UTXO Unspent 소비되지 않은 
	
	public PublicKey sender;
	public PublicKey recipient;
	public float value; // 5000
	public byte[] signature;// 100 privatkey publickey 
	public String transactionHash;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	
	public Transaction(PublicKey sender, PublicKey recipient, float value) {
		this.sender = sender ;
		this.recipient = recipient;
		this.value = value;
	}
	
	// 서명검증
	public boolean processTransaction(Transaction tx) {
		
		
		
		
		return false;
	}
	
	public void generateHash() {
		double nonce = Math.random();
		this.transactionHash = DigestUtils.sha256Hex(sender.toString()+recipient.toString()+value+nonce);
	}
	
	public void setHash(String hash) {
		this.transactionHash = hash;
	}
	
	public String getHash() {
		return this.transactionHash;
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = util.BlockUtil.getStringFromKey(sender)+util.BlockUtil.getStringFromKey(recipient)+Float.toString(value);
		signature = util.BlockUtil.applyECDSASig(privateKey, data);// 거래 data를 통해 시그니처가 생성되기 떄문에 data가 바뀌면 시그니쳐가 달라진다. 
	}
	
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	public boolean verifySignature() {
		String data = util.BlockUtil.getStringFromKey(sender)+util.BlockUtil.getStringFromKey(recipient)+Float.toString(value);
		return util.BlockUtil.verifyECDSASig(sender, data, signature);
	}
	
	

}
