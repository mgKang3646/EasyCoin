package model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import util.ECDSASignature;
import util.Encoding;

public class Transaction {
	private PublicKey sender;
	private PublicKey recipient;
	private float value; // 5000
	private byte[] signature;// 100 privatkey publickey 
	private String transactionHash;
	
	private ArrayList<TransactionInput> itxos = new ArrayList<TransactionInput>(); // 거래를 위한 UTXO
	private ArrayList<TransactionOutput> utxos = new ArrayList<TransactionOutput>(); // 거래 후 생성할 UTXO

	
	public Transaction(PublicKey sender, PublicKey recipient, float value) {
		this.sender = sender ;
		this.recipient = recipient;
		this.value = value;
	}
	
	public void generateHash() {
		double nonce = Math.random();
		this.transactionHash = DigestUtils.sha256Hex(sender.toString()+recipient.toString()+value+nonce);
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = Encoding.encodeKey(sender)+Encoding.encodeKey(recipient)+Float.toString(value);
		signature = ECDSASignature.applyECDSASig(privateKey, data);// 거래 data를 통해 시그니처가 생성되기 떄문에 data가 바뀌면 시그니쳐가 달라진다. 
	}
	
	public float getItxoSum() {
		float sum = 0;
		for(TransactionInput itxo : itxos) {
			sum += itxo.getInputValue();
		}
		return sum;
	}
	
	public ArrayList<TransactionInput> getItxoList() {
		return itxos;
	}
	
	public ArrayList<TransactionOutput> getUtxoList(){
		return utxos;
	}
	
	public PublicKey getSender() {
		return sender;
	}

	public PublicKey getRecipient() {
		return recipient;
	}

	public float getValue() {
		return value;
	}

	public byte[] getSignature() {
		return signature;
	}

	public String getTransactionHash() {
		return transactionHash;
	}
	
	public String getHash() {
		return this.transactionHash;
	}
	
	public void setItxoList(ArrayList<TransactionInput> itxos) {
		this.itxos = itxos;
	}
	
	public void setHash(String hash) {
		this.transactionHash = hash;
	}
	
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
		
}
