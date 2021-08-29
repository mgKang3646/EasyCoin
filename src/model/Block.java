package model;

import util.Encoding;

public class Block {
	private int num;
	private int nonce;
	private String timestamp;
	private	String previousBlockHash;
	private String hash;
	private String txData = "";
	private boolean isValid;
	
	public int getNum() {
		return num;
	}
	
	public int getNonce() {
		return nonce;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public String getPreviousBlockHash() {
		return previousBlockHash;
	}
	public String getHash() {
		return hash;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public void setNonce(int nonce) {
		this.nonce = nonce;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public void setPreviousBlockHash(String previousBlockHash) {
		this.previousBlockHash = previousBlockHash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public boolean isValid() {
		return isValid;
	}
	
	public String getTxData() {
		return txData;
	}
	public void setTxData(String txData) {
		this.txData = txData;
	}

	public void generateHash() {
		hash = Encoding.getSHA256HexHash(nonce + timestamp + previousBlockHash + txData);
	}
	public void verifyBlock(String inputHash) {
		if(inputHash.equals(getHash())) isValid = true;
		else isValid = false;
	}
}
