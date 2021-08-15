package model;

import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

public class Mining {
	
	public String hashDifficulty = "00000";
	
	private BlockChain blockchain;
	private Block minedBlock;
	private boolean miningFlag;
	private String hashString;
	private String timestamp;
	private String previousHash;
	private int blockNum;
	private int nonce;
	
	public Mining(BlockChain blockchain) {
		this.blockchain = blockchain;
	}
	
	public void setMiningFlag(boolean value) {
		this.miningFlag = value;
	}
	// 다시 채굴하는 경우 Null로 초기화 해주어야 함
	public boolean istmpBlockExisted() {
		if(blockchain.getTmpBlock() == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public Block mineBlock() {
		while(miningFlag) {
			setBlockComponents();
			System.out.println(hashString);
			if(isBlockHash()){
				createBlock();
				return minedBlock;
				//addBlockInBlockChain();
				//setMiningFlag(false);
			}
			
			if(istmpBlockExisted()) {
				setMiningFlag(false);
			}
		}
		
		return null;
	}
	
	private void createBlock() {
		minedBlock = new Block();
		minedBlock.setNum(blockNum);
		minedBlock.setHash(hashString);
		minedBlock.setPreviousBlockHash(previousHash);
		minedBlock.setNonce(nonce);
		minedBlock.setTimestamp(timestamp);
	}
	
	private void addBlockInBlockChain() {
		blockchain.addBlock(minedBlock);
	}
	
	private void setBlockComponents(){
		nonce++;	
		blockNum = blockchain.getBlockNum();
		previousHash = blockchain.getPreviousHash();
		timestamp = Long.toString(System.currentTimeMillis());
		hashString = DigestUtils.sha256Hex(nonce + timestamp + previousHash);
	}
	
	private boolean isBlockHash() {
		return hashString.substring(0,hashDifficulty.length()).equals(hashDifficulty);
	}
	
	
	
	
}
