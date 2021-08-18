package model;

import factory.JsonFactory;
import json.JsonSend;

public class Mining {
	
	public String hashDifficulty = "00000";
	
	private Peer peer;
	private ServerListener serverListener;
	private BlockVerify blockVerify;
	private BlockChain blockchain;
	private BlockMaker blockMaker;
	private PeerList peerList;
	private Block minedBlock;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	private boolean miningFlag;
	private String hashString;
	private int nonce;
	
	public Mining(Peer peer) {
		this.peer = peer;
		this.serverListener = peer.getServerListener();
		this.blockchain = peer.getBlockchain();
		this.blockVerify = blockchain.getBlockVerify();
		this.peerList = peer.getPeerList();
		this.jsonFactory = new JsonFactory();
		this.blockMaker = new BlockMaker();
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
			setMinedBlock();
			System.out.println(minedBlock.getHash());
			if(isBlockHash()){
				broadCastMinedBlock(); 
				blockVerify.verifyAfterMined(minedBlock);
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
				System.out.println("검증 결과 대기");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setMinedBlock(){
		this.minedBlock = blockMaker.makeMinedBlock(blockchain, ++nonce, Long.toString(System.currentTimeMillis()));
	}
	
	private boolean isBlockHash() {
		return minedBlock.getHash().substring(0,hashDifficulty.length()).equals(hashDifficulty);
	}
	
	// 채굴 클래스이지 보내는 클래스가 아님 분리 필요
	private void broadCastMinedBlock() {
		String msg = jsonSend.jsonBlockVerifyMessage(minedBlock);
		serverListener.send(msg);
	}
	
	
	
	
	
	
}
