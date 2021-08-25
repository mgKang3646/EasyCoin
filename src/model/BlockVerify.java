package model;

import controller.MiningController;
import factory.JsonFactory;
import javafx.application.Platform;
import json.JsonSend;
import newview.FxmlLoader;
import newview.NewView;
import newview.ViewURL;

public class BlockVerify {
	
	private Block tmpBlock;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	private NewView newView;
	private int grantedNum;
	private boolean isVerifying;
	private boolean isFirst;
	private boolean isTmpBlockGranted;
	private boolean isMinedBlock;

	
	public BlockVerify() {
		jsonFactory = new JsonFactory();
		isFirst = true;
		newView = new NewView();
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
				MiningController mc = FxmlLoader.getFXMLLoader(ViewURL.miningURL).getController();
				mc.verifyUI();
				sleepThread(5000);
				setTmpBlockGranted();
				if(isTmpBlockGranted) BlockChain.getBlocklist().addBlock(tmpBlock);
				setVerifying(false);
				mc.basicUI();
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
				if(isMinedBlock) newView.getNewWindow(ViewURL.miningResultURL,MiningState.MININGGRANTED);
				else newView.getNewWindow(ViewURL.miningResultURL,MiningState.OTHERMININGGRANTED);
			}else {
				 newView.getNewWindow(ViewURL.miningResultURL,MiningState.FAILEDGRANTED);
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
		jsonSend = jsonFactory.getJsonSend(P2PNet.getServerListener());
		jsonSend.sendBlockMinedMessage(tmpBlock);
	}
	
	public void broadCastingVerifiedResult() {
		jsonSend = jsonFactory.getJsonSend(P2PNet.getServerListener());
		jsonSend.sendVerifiedResultMessage(tmpBlock.isValid());
	}
	
	private void setVerifying(boolean value) {
		isVerifying = value;
	}
	
	private double getTotal() {
		return Peer.peerList.getSize()+1;
	}
	
	private void printTmpBlockGrantedRate() {
		System.out.println( "grantedNum : " + grantedNum +" total : " +getTotal() + " rate : " + grantedNum/getTotal());
	}
}
