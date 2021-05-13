package model;

import java.security.PublicKey;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Base64;

//UTXO 70 ä���� ö�� ,UTXO 30 ä���� �μ�, UTXO 50 ä���� ����
//UTXO 50 �α� => UTXO ö�� 30, UTXO �α� 20
public class TransactionOutput {
	
	String miner;
	PublicKey recipient;
	float value;
	String txoHash;
	int nonce;
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
		this.nonce = (int)(Math.random()*1000000);
		String recipientEncoding = Base64.toBase64String(recipient.getEncoded());
		this.txoHash = DigestUtils.sha256Hex(miner+recipientEncoding+value+nonce);
	}
	public void setHash(String hash) {
		this.txoHash = hash;
	}
	
	public int getNonce() {
		return nonce;
	}
	public void setNonce(int nonce) {
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
	
	@Override
	public String toString() {

		String print = "[ value : " + getValue() + " ]";
		return print;
	}



}
