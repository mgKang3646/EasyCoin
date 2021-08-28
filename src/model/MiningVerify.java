package model;

public class MiningVerify {

	
	public void doVerify(Block tmpBlock) {
		if(!BlockChain.getBlockVerify().getBlockVerifyState().isVerifying()) BlockChain.getBlockVerify().verifyMyMinedBlock(tmpBlock);
	}
	
	public void waitVerifying() {
		while(BlockChain.getBlockVerify().getBlockVerifyState().isVerifying()) {
			try {
				System.out.println("���� ��� ���");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
