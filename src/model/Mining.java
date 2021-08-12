package model;

import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

public class Mining {
	
	public String hashDifficulty = "00000";
	
	private BlockChain blockchain;
	private Block minedBlock;
	private boolean miningFlag;
	
	private String hashString;
	private String currentTime;
	private String previousHash;
	private int blockNum;
	private int nonce;
	
	public Mining(BlockChain blockchain) {
		this.blockchain = blockchain;
	}
	
	public void setMiningFlag(boolean value) {
		this.miningFlag = value;
	}
	
	public boolean getMiningFlag() {
		return this.miningFlag;
	}
	
	public void mineBlock() {
		while(miningFlag) {
			setBlockComponents();
			System.out.println(hashString);
			if(isBlockHash()){
				createBlock();
				addBlockInBlockChain();
				setMiningFlag(false);
			}
		}
	}
	
	private void createBlock() {
		minedBlock = new Block();
		minedBlock.setNum(blockNum);
		minedBlock.setHash(hashString);
		minedBlock.setPreviousBlockHash(previousHash);
		minedBlock.setNonce(nonce);
		minedBlock.setTimestamp(currentTime);
	}
	
	private void addBlockInBlockChain() {
		blockchain.addBlock(minedBlock);
	}
	
	private void setBlockComponents(){
		nonce++;	
		blockNum = blockchain.getBlockNum();
		previousHash = blockchain.getPreviousHash();
		currentTime = Long.toString(System.currentTimeMillis());
		hashString = DigestUtils.sha256Hex(previousHash+nonce+currentTime);
	}
	
	private boolean isBlockHash() {
		return hashString.substring(0,hashDifficulty.length()).equals(hashDifficulty);
	}
	
	
	
	
}
