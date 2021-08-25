package model;

public class BlockChain {
	public static final BlockList blockList = new BlockList();
	public static final BlockVerify blockVerify = new BlockVerify();
	
	public static BlockList getBlocklist() {
		return blockList;
	}
	public static BlockVerify getBlockverify() {
		return blockVerify;
	}
	
	
}
