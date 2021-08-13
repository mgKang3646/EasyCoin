package model;

public class Block {
	private int num;
	private int nonce;
	private String timestamp;
	private	String previousBlockHash;
	private String hash;
	
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
	
	public String getDataForHash() {
		return previousBlockHash + timestamp + nonce;
	}
}