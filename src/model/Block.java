package model;

import org.apache.commons.codec.digest.DigestUtils;


public class Block {

	private int num;
	private String nonce = null;
	private String timestamp = null;
	private	String previousBlockHash = null;
	private String hash = null;
	public static int count=0;

	public Block(String previousBlockHash, String nonce,String timestamp,int num) {
		this.previousBlockHash = previousBlockHash; // 이전 블록의 해쉬
		this.nonce = nonce; // 넌스
		this.timestamp = timestamp;
		this.hash = DigestUtils.sha256Hex(this.previousBlockHash+this.nonce+this.timestamp);
		this.num = num;
	}

	public String getNonce() { return nonce; }
	public String getTimestamp() { return timestamp; }
	public String getPreviousBlockHash() { return previousBlockHash; }
	public String getHash() { return hash; }
	public int getNum() { return num; }
}