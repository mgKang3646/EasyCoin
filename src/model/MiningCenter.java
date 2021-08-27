package model;

import controller.MiningController;
import newview.FxmlLoader;
import newview.ViewURL;

public class MiningCenter {

	private Mining mining;
	private MiningVerify miningVerify;
	private MiningController miningController;
	private BlockVerify blockVerify;
	private boolean isStop;
	
	public MiningCenter() {
		mining = new Mining();
		miningVerify = new MiningVerify();
	}
	
	public void stop() {
		mining.setMiningFlag(false);
		miningController.stopUI();
		setIsStop(true);
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
		blockVerify = BlockChain.getBlockverify();
		miningController = FxmlLoader.getFXMLLoader(ViewURL.miningURL).getController();
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
				mining.getMinedBlock().setValid(true);
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
