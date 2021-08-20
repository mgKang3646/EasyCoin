package model;

import controller.MiningController;
import factory.JsonFactory;
import json.JsonSend;

public class MiningCenter {

	private Peer peer;
	private Mining mining;
	private MiningVerify miningVerify;
	private BlockChain blockchain;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	private MiningState miningResult;
	private MiningState verifyResult;
	private MiningController miningController;
	private boolean isStop;

	
	public MiningCenter(Peer peer) {
		this.peer = peer;
		this.mining = new Mining(peer);
		this.jsonFactory = new JsonFactory();
		this.blockchain = peer.getBlockchain();
		this.jsonSend = jsonFactory.getJsonSend(peer.getServerListener());
		this.miningVerify = new MiningVerify(peer.getBlockchain().getBlockVerify());
	}
	
	public void setMiningController(MiningController miningController) {
		this.miningController = miningController;
	}
	
	public MiningState getVerifyResult() {
		return verifyResult;
	}
	public void start() {
		Thread thread = new Thread() {
			public void run() {
				initialize();
				doMining();
				if(!isStop) {
					broadCasting();
					doVerifying();
					addBlock();
					miningController.stopUI();
					miningController.viewResult();
				}
			}
		};
		thread.start();
	}
	
	public void stop() {
		isStop = true;
		mining.setMiningFlag(false);
		miningController.stopUI();
	}
	
	private void initialize() {
		miningResult = null;
		verifyResult = null;
	}
		
	private void doMining() {
		miningController.startUI();
		mining.setMiningFlag(true);
		miningResult = mining.mineBlock();
	}
	
	private void broadCasting() {
		switch(miningResult) {
			case MININGSUCCESS : jsonSend.sendBlockMinedMessage(miningResult.getBlock()); break;
			case OTHERMININGSUCCESS : break;
			case HALT : break;
			default : break;
		}
	} 

	private void doVerifying() {
		miningController.verifyUI();
		switch(miningResult) {
			case MININGSUCCESS : verifyResult = miningVerify.doVerify(miningResult.getBlock()); break;
			case OTHERMININGSUCCESS : verifyResult = miningVerify.doVerify(); break;
			case HALT : break;
			default : break;
		}
	}
	
	private void addBlock() {
		switch(verifyResult) {
			case MININGVERIFIED : blockchain.addBlock(verifyResult.getBlock()); break;
			case OTHERMININGVERIFIED : blockchain.addBlock(verifyResult.getBlock()); break;
			case FAILEDVERIFY : break;
			default : break;
		}
	}

}
