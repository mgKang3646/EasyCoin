package model;

public class Block {
	private int num;
	private String nonce = null;
	private String timestamp = null;
	private	String previousBlockHash = null;
	private String hash = null;
	
	public int getNum() {
		return num;
	}
	public String getNonce() {
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
	public void setNonce(String nonce) {
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
