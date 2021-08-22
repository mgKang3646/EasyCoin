package model;

import controller.MiningController;

public class MiningCenter {

	private Peer peer;
	private Mining mining;
	private MiningVerify miningVerify;
	private MiningController miningController;
	private BlockVerify blockVerify;
	private boolean isStop;

	public void initializeObjects(Peer peer) {
		this.peer = peer;
		this.mining = new Mining(peer);
		this.blockVerify = peer.getBlockchain().getBlockVerify();
		this.miningVerify = new MiningVerify(blockVerify);
	}
	
	public void setMiningController(MiningController miningController) {
		this.miningController = miningController;
	}
	
	public void stop() {
		mining.setMiningFlag(false);
		setIsStop(true);
		miningController.stopUI();
		Thread.yield();
	}
	
	public void start() {
		Thread thread = new Thread() {
			public void run() {
				startRoutine();
				miningRoutine();
				verifyRoutine();
			}
		};
		thread.start();
	}
	
	private void startRoutine() {
		miningController.startUI();
		mining.startMining();
		initialize();
	}
	
	private void miningRoutine() {
		while(!blockVerify.isVerifying()) {
			Thread.onSpinWait();
			if(isStop) break;
			if(mining.isMined()) {
				miningController.verifyUI();
				miningVerify.doVerify(mining.getMinedBlock());
				break;
			}
		}
	}
	
	private void verifyRoutine() {
		if(!isStop) {
			miningController.verifyUI();
			miningVerify.waitVerifying();
			miningController.stopUI();
		}
	}
	
	private void initialize() {
		mining.setIsMined(false);
		mining.setMiningFlag(true);
		setIsStop(false);
	}
	
	private void setIsStop(boolean value) {
		isStop = value;
	}
}
