package model;

import org.apache.commons.codec.digest.DigestUtils;

public class Block {
	private int num;
	private int nonce;
	private String timestamp;
	private	String previousBlockHash;
	private String hash;
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
	public void generateHash() {
		hash = DigestUtils.sha256Hex(nonce + timestamp + previousBlockHash);
	}
	public void verifyBlock(String inputHash) {
		if(inputHash.equals(getHash())) isValid = true;
		else isValid = false;
	}
}
