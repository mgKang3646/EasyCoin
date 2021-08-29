package model;

import util.ThreadUtil;

public class BlockVerify {
	
	private Block tmpBlock;
	private int grantedNum;
	private BlockVerifyState blockVerifyState;
	private MiningState verifyResult;

	
	public BlockVerify() {
		blockVerifyState = new BlockVerifyState();
	}
	
	public BlockVerifyState getBlockVerifyState() {
		return blockVerifyState;
	}
	
	public void verifyOtherMinedBlock(Block tmpBlock) {
		setTmpBlock(tmpBlock);
		blockVerifyState.setMinedBlock(false);
		doPoll(true); // 채굴자
		doPoll(tmpBlock.isValid()); // 검증자
		waitOtherPeerPoll();
	}
	
	public void verifyMyMinedBlock(Block tmpBlock) {
		setTmpBlock(tmpBlock);
		blockVerifyState.setMinedBlock(true);
		BlockChain.getBlockChainSend().broadCastingMinedBlock(tmpBlock);
		doPoll(true);
		waitOtherPeerPoll();
	}
	
	public void handleVerifyResult(boolean verifyResult) {
		doPoll(verifyResult);
		waitOtherPeerPoll();
	}
	
	private void setTmpBlock(Block tmpBlock) {
		this.tmpBlock = tmpBlock;
	}
	
	private void doPoll(boolean result) {
		if(result) grantedNum++;
	}
	
	private void waitOtherPeerPoll() {
		if(blockVerifyState.isFirst()) {
			blockVerifyState.setFirst(false);
			blockVerifyState.setVerifying(true);
			startPoll();
		}
	}
	
	private void startPoll()  {
		Thread thread = new Thread() {
			public void run() {
				BlockChain.getBlockChainView().startVerify();
				ThreadUtil.sleepThread(5000);
				setTmpBlockGranted();
				setResult();
				applyBlock();
				reward();
				Wallet.txList.processTxList(verifyResult);
				Wallet.txList.resetTxList();
				ThreadUtil.sleepThread(2000);
				Wallet.itxo.resetItxoList();
				BlockChain.getBlockChainView().endVerify();
				BlockChain.getBlockChainView().showResult(verifyResult);
				BlockChain.resetBLockVerify();
				blockVerifyState.setVerifying(false);
			}
		};
		thread.start();
	}

	private void setTmpBlockGranted() {
		printTmpBlockGrantedRate();
		if( grantedNum / getTotal() >= 0.51 ) blockVerifyState.setTmpBlockGranted(true); 
		else blockVerifyState.setTmpBlockGranted(false);
	}
	
	private void setResult() {
		if(blockVerifyState.isTmpBlockGranted()&&tmpBlock.isValid()) {
			if(blockVerifyState.isMinedBlock()) verifyResult = MiningState.MININGGRANTED;
			else verifyResult = MiningState.OTHERMININGGRANTED;
		}else verifyResult = MiningState.FAILEDGRANTED;
	}
	
	private void applyBlock() {
		if(blockVerifyState.isTmpBlockGranted()&&tmpBlock.isValid()) BlockChain.getBlocklist().applyBlock(tmpBlock);
	}
	
	public void reward() {
		if( verifyResult == MiningState.MININGGRANTED ) {
			if(Peer.myPeer.getPublicKey() != null) {
				Wallet.utxo.addRewardUTXO();
			}
		}
	}
	
	public MiningState getResult() {
		return verifyResult;
	}
	
	public Block getTmpBlock() {
		return tmpBlock;
	}

	private double getTotal() {
		return Peer.peerList.getSize()+1;
	}
	
	private void printTmpBlockGrantedRate() {
		System.out.println( "grantedNum : " + grantedNum +" total : " +getTotal() + " rate : " + grantedNum/getTotal());
	}
}
