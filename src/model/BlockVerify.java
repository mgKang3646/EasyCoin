package model;

import javax.json.JsonObject;


public class BlockVerify {
	
	private Peer peer;
	private Block tmpBlock;
	private BlockMaker blockMaker;
	private String inputHash;
	private boolean isTmpBlockValid;
	private boolean isFirst;
	private boolean isVerifying;
	private boolean verifyResult;
	private int verifiedNum;

	
	public BlockVerify(Peer peer) {
		this.peer = peer;
		this.isFirst = true;
		this.blockMaker = new BlockMaker();
	}
	
	public void verifyBeforeMined(JsonObject jsonObject) {
		setIsVerifying(true);
		addVerifyResult(true); // Ã¤±¼ÀÚ Áõ°¡
		setInputHash(jsonObject);
		generateTmpBlock(jsonObject);
		verifyHash();
		handleVerifyResult(verifyResult);
	}
	
	public void verifyAfterMined(Block tmpBlock) {
		setIsVerifying(true);
		setTmpBlock(tmpBlock);
		handleVerifyResult(true);
	}
	
	public void handleVerifyResult(boolean result) {
		addVerifyResult(result);
		waitVerify();
	}
	
	public void initialize() {
		tmpBlock = null;
		inputHash = null;
		verifiedNum = 0;
		isTmpBlockValid = false;
		isFirst = true;
		isVerifying = false;
		verifyResult = false;
	}
	
	private void setInputHash(JsonObject jsonObject) {
		inputHash = jsonObject.getString("hash");
	}
	
	private void generateTmpBlock(JsonObject jsonObject) {
		tmpBlock = blockMaker.makeTmpBlock(jsonObject, peer.getBlockchain().getPreviousHash());
	}
	
	private void verifyHash() {
		if(inputHash.equals(tmpBlock.getHash())) {
			verifyResult = true;
		}
		else {
			verifyResult = false;
		}
	}
	
	private void addVerifyResult(boolean result) {
		if(result) verifiedNum++;
	}
	
	private void waitVerify() {
		if(isFirst) {
			isFirst = false;
			calculateVerifyRate();
		}
	}
	
	private void calculateVerifyRate()  {
		Thread thread = new Thread() {
			public void run() {
				sleepThread(5000);
				checkTmpBlockVaild();
				setIsVerifying(false);
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
	
	private void checkTmpBlockVaild() {
		System.out.println( "verifiedNum : " + verifiedNum +" total : " +getTotal() + " rate : " + verifiedNum/getTotal());
		if( verifiedNum / getTotal() >= 0.51 ) {
			isTmpBlockValid = true;
		}else {
			isTmpBlockValid = false;
		}
	}
	
	private void setIsVerifying(boolean value) {
		isVerifying = value;
	}
	
	public boolean getVerifyResult() {
		return verifyResult;
	}
	
	public Block getTmpBlock() {
		return tmpBlock;
	}
	
	public void setTmpBlock(Block tmpBlock) {
		this.tmpBlock = tmpBlock;
	}
	
	public boolean isVerifying() {
		return isVerifying;
	}
		
	public boolean isTmpBlockValid() {
		return isTmpBlockValid;
	}
	
	public boolean isTmpBlockExisted() {
		if(tmpBlock != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public double getTotal() {
		return peer.getPeerList().getSize()+1;
	}
	
	
	
	
	
	
	
	
	
	
}
