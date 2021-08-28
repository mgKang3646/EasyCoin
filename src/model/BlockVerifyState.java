package model;

public class BlockVerifyState {
	
	private boolean isVerifying;
	private boolean isFirst = true;
	private boolean isTmpBlockGranted;
	private boolean isMinedBlock;
	
	public boolean isVerifying() {
		return isVerifying;
	}
	public boolean isFirst() {
		return isFirst;
	}
	public boolean isTmpBlockGranted() {
		return isTmpBlockGranted;
	}
	public boolean isMinedBlock() {
		return isMinedBlock;
	}
	public void setVerifying(boolean isVerifying) {
		this.isVerifying = isVerifying;
	}
	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	public void setTmpBlockGranted(boolean isTmpBlockGranted) {
		this.isTmpBlockGranted = isTmpBlockGranted;
	}
	public void setMinedBlock(boolean isMinedBlock) {
		this.isMinedBlock = isMinedBlock;
	}
}
