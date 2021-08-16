package model;

import javax.json.JsonObject;

public class BlockVerify {

	private Block tmpBlock;
	private String inputHash;
	private boolean isTmpBlockValid;
	private boolean isNotFirst;
	private boolean isVerifying;
	private int verifiedNum;
	private double total;
	
	
	public void setTmpBlock(Block tmpBlock) {
		this.tmpBlock = tmpBlock;
	}
	
	public void setTotal(int total) {
		this.total = total + 1;
	}
	
	public void setIsTmpBlockValid(boolean result) {
		isTmpBlockValid = result;
	}
	
	public void setIsVerifying(boolean result) {
		isVerifying = result;
	}
	
	public void handleVerifyResult(boolean result) {
		if(isNotFirst) {
			addVerifyResult(result);
		}else {
			isNotFirst = true;
			addVerifyResult(result);
			calculateVerifyRate();
		}
	}
	
	
	private void addVerifyResult(boolean result) {
		if(result) verifiedNum++;
	}
	
	public Block getTmpBlock() {
		return tmpBlock;
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
	
	public boolean isVerifying() {
		return isVerifying;
	}
	
	public void doVerify(JsonObject object) {
		setIsVerifying(true);
		generateTmpBlock(object);
		verify();
	}
	
	private void verify() {
		if(inputHash.equals(tmpBlock.getHash())) isTmpBlockValid = true;
		else isTmpBlockValid = false;
	}
	
	private void calculateVerifyRate() {
		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
					checkTmpBlockVaild();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.run();
	}
	
	private void checkTmpBlockVaild() {
		System.out.println( "verifiedNum : " + verifiedNum +" total : " +total + " rate : " + verifiedNum/total);
		if( verifiedNum / total >= 0.51 ) {
			setIsTmpBlockValid(true);
		}else {
			setIsTmpBlockValid(false);
		}
	}
	
	private void generateTmpBlock(JsonObject object) {
		inputHash = object.getString("hash");
		tmpBlock = new Block();
		tmpBlock.setNonce(object.getInt("nonce"));
		tmpBlock.setTimestamp(object.getString("timestamp"));
		tmpBlock.setPreviousBlockHash(object.getString("previousHash")); // 본인이 갖고 있는 마지막블록의 이전해쉬여야함.
		tmpBlock.generateHash();
	}
	
	private void initialize() {
		tmpBlock = new Block();
		inputHash = null;
		verifiedNum = 0;
		isTmpBlockValid = false;
		isNotFirst = false;
		isVerifying = false;
	}
	
	
	
	
	
}
