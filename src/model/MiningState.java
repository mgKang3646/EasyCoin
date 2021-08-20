package model;

public enum MiningState {
	MININGSUCCESS,
	OTHERMININGSUCCESS,
	HALT,
	MININGVERIFIED,
	OTHERMININGVERIFIED,
	SUCCESSVERIFY,
	FAILEDVERIFY;
	
	private Block block;

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}
}
