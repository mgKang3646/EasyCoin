package model;

public class MiningVerify {

	private BlockVerify blockVerify;
	public MiningVerify(BlockVerify blockVerify) {
		this.blockVerify = blockVerify;
	}
	
	public void doVerify(Block tmpBlock) {
		if(!blockVerify.isVerifying()) {
			blockVerify.setTmpBlock(tmpBlock);
			blockVerify.setIsMinedBlock(true);
			blockVerify.broadCastingMinedBlock();
			blockVerify.doPoll(true);
			blockVerify.waitOtherPeerPoll();
		}
	}
	
	public void waitVerifying() {
		while(blockVerify.isVerifying()) {
			try {
				System.out.println("검증 결과 대기");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
