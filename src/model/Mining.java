package model;

import factory.JsonFactory;
import json.JsonSend;

public class Mining {
	
	public String hashDifficulty = "0000";
	
	private Peer peer;
	private ServerListener serverListener;
	private BlockVerify blockVerify;
	private BlockChain blockchain;
	private BlockMaker blockMaker;
	private Block minedBlock;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	private boolean miningFlag;
	private int nonce;
	
	public Mining(Peer peer) {
		this.peer = peer;
		this.serverListener = peer.getServerListener();
		this.blockchain = peer.getBlockchain();
		this.blockVerify = blockchain.getBlockVerify();
		this.jsonFactory = new JsonFactory();
		this.jsonSend = jsonFactory.getJsonSend(serverListener);
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
			if(isBlockHash()){
				jsonSend.sendBlockMinedMessage(minedBlock); 
				blockVerify.verifyAfterMined(minedBlock);
				return getResultAfterMined();
			}
			if(istmpBlockExisted()) {
				return getResultBeforeMined();
			}
		}
		return MiningState.NONE;
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
	
	private MiningState getResultAfterMined() {
		if(isVerify()) {
			blockchain.addTmpBlock();
			return MiningState.SUCCESSMINING;
		}else {
			return MiningState.FAILEDVERIFY;
		}	
	}

	private MiningState getResultBeforeMined() {
		if(isVerify()) {
			blockchain.addTmpBlock();
			 return MiningState.SUCCESSVERIFY;
		}else {
			 return MiningState.FAILEDVERIFY;
		}	
	}
	
	private boolean isVerify() {
		waitVerifyResult();
		return blockVerify.isTmpBlockValid();
	}
	
	private void waitVerifyResult() {
		while(blockVerify.isVerifying()) {
			try {
				System.out.println("검증 결과 대기");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
