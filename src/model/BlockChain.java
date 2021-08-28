package model;

public class BlockChain {
	public static final BlockList blockList = new BlockList();
	public static BlockVerify blockVerify = new BlockVerify();
	public static BlockChainView blockChainView = new BlockChainView();
	public static BlockChainSend blockChainSend = new BlockChainSend();
	
	
	public static BlockList getBlocklist() {
		return blockList;
	}
	public static BlockVerify getBlockVerify() {
		return blockVerify;
	}
	public static BlockChainView getBlockChainView() {
		return blockChainView;
	}
	public static BlockChainSend getBlockChainSend() {
		return blockChainSend;
	}
	public static void resetBLockVerify() {
		blockVerify = new BlockVerify();
	}
	
	
}
