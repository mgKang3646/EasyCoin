package model;

import factory.JsonFactory;
import json.JsonSend;

public class Mining {
	
	public String hashDifficulty = "00000";
	
	private BlockVerify blockVerify;
	private BlockChain blockchain;
	private PeerList peerList;
	private Block minedBlock;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	private boolean miningFlag;
	private String hashString;
	private int nonce;
	
	public Mining(Peer peer) {
		this.blockchain = peer.getBlockchain();
		this.blockVerify = blockchain.getBlockVerify();
		this.peerList = peer.getPeerList();
		this.minedBlock = new Block();
		this.jsonFactory = new JsonFactory();
		this.jsonSend = jsonFactory.getJsonSend();
	}
	
	public void setMiningFlag(boolean value) {
		this.miningFlag = value;
	}
	public boolean istmpBlockExisted() {
		return blockVerify.isTmpBlockExisted(); 
	}
	
	public MiningState mineBlock() {
		while(miningFlag) {
			setBlockComponents();
			System.out.println(minedBlock.getHash());
			if(isBlockHash()){
				broadCastMinedBlock();
				blockVerify.setTotal(peerList.getSize());
				blockVerify.setTmpBlock(minedBlock);
				blockVerify.handleVerifyResult(true);	
				if(isVerify()) {
					blockchain.addTmpBlock();
					return MiningState.SUCCESSMINING;
				}else {
					return MiningState.FAILEDVERIFY;
				}
			}
			
			if(istmpBlockExisted()) {
				if(isVerify()) {
					blockchain.addTmpBlock();
					return MiningState.SUCCESSVERIFY;
				}else {
					return MiningState.FAILEDVERIFY;
				}
			}
		}
		return MiningState.NONE;
	}
	
	private boolean isVerify() {
		waitVerifyResult();
		return blockVerify.isTmpBlockValid();
	}
	
	private void waitVerifyResult() {
		while(blockVerify.isVerifying()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setBlockComponents(){
		minedBlock.setNum(blockchain.getBlockNum());
		minedBlock.setPreviousBlockHash( blockchain.getPreviousHash());
		minedBlock.setNonce(++nonce);
		minedBlock.setTimestamp( Long.toString(System.currentTimeMillis()));
		minedBlock.generateHash();
	}
	
	private boolean isBlockHash() {
		return minedBlock.getHash().substring(0,hashDifficulty.length()).equals(hashDifficulty);
	}
	
	private void broadCastMinedBlock() {
		jsonSend.jsonBlockVerifyMessage(minedBlock);
	}
	
	
	
	
}
