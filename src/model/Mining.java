package model;

import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

public class Mining {
	
	public String hashDifficulty = "00000";
	private BlockChain blockchain;
	private Block miningBlock;
	private Block previousBlock;
	private boolean miningFlag;
	private String hashString;
	private String currentTime;
	private String previousHash;
	private int nonce;
	
	public Mining(BlockChain blockchain) {
		this.blockchain = blockchain;
	}
	
	public String mineBlock() {
		createBlock();
		while(miningFlag) {
			setMiningComponents();
			System.out.println(hashString);
			if(isBlockHash()){
				setMiningFlag(false);
			}
		}
		return hashString;
	}
	
	private void createBlock() {
		miningBlock = new Block();
		previousBlock = blockchain.getBlocks().get(blockchain.getBlocks().size()-1);
	}
	
	private void setMiningComponents(){
		nonce++;	
		previousHash = previousBlock.getHash();
		currentTime = Long.toString(System.currentTimeMillis());
		hashString = DigestUtils.sha256Hex(previousHash+nonce+currentTime);
	}
	
	private boolean isBlockHash() {
		return hashString.substring(0,hashDifficulty.length()).equals(hashDifficulty);
	}
	
	public void setMiningFlag(boolean value) {
		this.miningFlag = value;
	}
	
	public boolean getMiningFlag() {
		return this.miningFlag;
	}
	
}
