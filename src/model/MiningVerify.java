package model;

public class MiningVerify {

	private BlockVerify blockVerify;
	private MiningState result;
	
	public MiningVerify(BlockVerify blockVerify) {
		this.blockVerify = blockVerify;
	}
	
	public MiningState doVerify(Block minedBlock) {
		blockVerify.initialize();
		blockVerify.verifyAfterMined(minedBlock);
		return getMinedBlockVerifyResult();
	}
	
	public MiningState doVerify() {
		return getOtherMinedBlockVerifyResult();
	}
	
	private MiningState getMinedBlockVerifyResult() {
		if(isVerify()) {
			setMiningState(MiningState.MININGVERIFIED);
		}else {
			setMiningState(MiningState.FAILEDVERIFY);
		}	
		return result;
	}
	
	private MiningState getOtherMinedBlockVerifyResult() {
		if(isVerify()) {
			setMiningState(MiningState.OTHERMININGVERIFIED);
		}else {
			setMiningState(MiningState.FAILEDVERIFY);
		}	
		return result;
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
	
	private void setMiningState(MiningState miningState) {
		result = miningState;
		result.setBlock(blockVerify.getTmpBlock());
	}
}
