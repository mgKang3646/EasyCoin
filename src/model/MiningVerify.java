package model;

public class MiningVerify {

	private BlockVerify blockVerify;
	public MiningVerify() {
		blockVerify = BlockChain.getBlockverify();
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
				System.out.println("���� ��� ���");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
