package model;

import javax.json.JsonObject;

import org.apache.commons.codec.digest.DigestUtils;

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
	public void generateHash() {
		hash = DigestUtils.sha256Hex(nonce + timestamp + previousBlockHash);
	}
	
	public void setGenesisBlock() {
		setPreviousBlockHash("0000000000000000000000000000000000000000000000000000000000000000");
		setNonce(0);
		setTimestamp("00000000");
		setNum(0);
		generateHash();
	}
	
	public void setTmpBlock(JsonObject jsonObject, String previousHash) {
		setNum(jsonObject.getInt("blockNum"));
		setNonce(jsonObject.getInt("nonce"));
		setTimestamp(jsonObject.getString("timestamp"));
		setPreviousBlockHash(previousHash); // 본인이 갖고 있는 마지막블록의 이전해쉬여야함.
		generateHash();
	}

}
