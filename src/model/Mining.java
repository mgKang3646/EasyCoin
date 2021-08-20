package model;

import factory.JsonFactory;
import json.JsonSend;

public class Mining {
	
	public String hashDifficulty = "0000";
	
	private Peer peer;
	private MiningState miningState;
	private BlockVerify blockVerify;
	private BlockChain blockchain;
	private BlockMaker blockMaker;
	private Block minedBlock;
	private boolean miningFlag;
	private int nonce;
	
	public Mining(Peer peer) {
		this.peer = peer;
		this.blockchain = peer.getBlockchain();
		this.blockVerify = blockchain.getBlockVerify();
		this.blockMaker = new BlockMaker();
	}
	
	public void setMiningFlag(boolean value) {
		this.miningFlag = value;
	}
	public boolean istmpBlockExisted() {
		return blockVerify.isTmpBlockExisted(); 
	}
	
	public MiningState mineBlock() {
		while(miningFlag) {
			setMinedBlock();
			printHashString();
			if(isBlockHash()) {
				miningState = MiningState.MININGSUCCESS;
				miningState.setBlock(minedBlock);
				return miningState;
			}
			if(istmpBlockExisted()) {
				miningState = MiningState.OTHERMININGSUCCESS;
				return miningState;
			}
		}
		miningState = MiningState.HALT;
		return miningState;
	}
	
	private void setMinedBlock(){
		this.minedBlock = blockMaker.makeMinedBlock(blockchain, ++nonce, Long.toString(System.currentTimeMillis()));
	}
	
	private void printHashString() {
		System.out.println(minedBlock.getHash());
	}
	
	private boolean isBlockHash() {
		return minedBlock.getHash().substring(0,hashDifficulty.length()).equals(hashDifficulty);
	}
}
