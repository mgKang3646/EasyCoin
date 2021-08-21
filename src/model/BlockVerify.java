package model;

import factory.JsonFactory;
import factory.NewPageFactory;
import javafx.application.Platform;
import json.JsonSend;

public class BlockVerify {
	
	private Peer peer;
	private Block tmpBlock;
	private boolean isVerifying;
	private boolean isFirst;
	private boolean isTmpBlockGranted;
	private boolean isMinedBlock;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	private int grantedNum;
	private NewPageFactory newPageFactory;

	
	public BlockVerify(Peer peer) {
		this.peer = peer;
		this.jsonFactory = new JsonFactory();
		this.newPageFactory = new NewPageFactory();
		this.isFirst = true;
	}
	
	public void setTmpBlock(Block tmpBlock) {
		this.tmpBlock = tmpBlock;
	}
	
	public void doPoll(boolean result) {
		if(result) grantedNum++;
	}
	
	public void waitOtherPeerPoll() {
		if(isFirst) {
			isFirst = false;
			setVerifying(true);
			startPoll();
		}
	}
	
	public void initialize() {
		tmpBlock = null;
		grantedNum = 0;
		isTmpBlockGranted = false;
		isVerifying = false;
		isFirst = true;
	}
	
	private void startPoll()  {
		Thread thread = new Thread() {
			public void run() {
				sleepThread(5000);
				setTmpBlockGranted();
				if(isTmpBlockGranted) peer.getBlockchain().addTmpBlock();
				setVerifying(false);
				viewResult();
			}
		};
		thread.start();
	}

	
	
	private void sleepThread(int time) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void setTmpBlockGranted() {
		printTmpBlockGrantedRate();
		if( grantedNum / getTotal() >= 0.51 ) {
			isTmpBlockGranted = true;
		}else {
			isTmpBlockGranted = false;
		}
	}
	
	private void viewResult() { 
		Platform.runLater(()->{
			if(isTmpBlockGranted) {
				if(isMinedBlock) newPageFactory.createMiningResult(peer, MiningState.MININGGRANTED);
				else newPageFactory.createMiningResult(peer, MiningState.OTHERMININGGRANTED);
			}else {
				newPageFactory.createMiningResult(peer, MiningState.FAILEDGRANTED);
			}
			initialize(); // 관심사 분리 요망
		});
	}
	
	
	public Block getTmpBlock() {
		return tmpBlock;
	}
	
	public boolean isTmpBlockGranted() {
		return isTmpBlockGranted;
	}
	
	public boolean isFirst() {
		return isFirst();
	}
	
	public boolean isVerifying() {
		return isVerifying;
	}
	
	public void setIsMinedBlock(boolean value) {
		isMinedBlock = value;
	}
	
	public void broadCastingMinedBlock() {
		jsonSend = jsonFactory.getJsonSend(peer.getServerListener());
		jsonSend.sendBlockMinedMessage(tmpBlock);
	}
	
	public void broadCastingVerifiedResult() {
		jsonSend = jsonFactory.getJsonSend(peer.getServerListener());
		jsonSend.sendVerifiedResultMessage(tmpBlock.isValid());
	}
	
	private void setVerifying(boolean value) {
		isVerifying = value;
	}
	
	private double getTotal() {
		return peer.getPeerList().getSize()+1;
	}
	
	private void printTmpBlockGrantedRate() {
		System.out.println( "grantedNum : " + grantedNum +" total : " +getTotal() + " rate : " + grantedNum/getTotal());
	}
}
