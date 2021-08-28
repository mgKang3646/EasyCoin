package model;

import java.util.ArrayList;
import database.Dao;

public class BlockList {
	
	private ArrayList<Block> blocks;
	private Dao dao;
	
	public BlockList() {
		blocks = new ArrayList<Block>();
		dao = new Dao();
	}
	
	public ArrayList<Block> getBlocks() { 
		return blocks; 
	}
	
	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
	public void applyBlock(Block block) {
		blocks.add(block);
		dao.storeBlock(block);
	}
	
	public int getBlockNum() {
		return blocks.size();
	}
	
	public Block getLastBlock() {
		return blocks.get(blocks.size()-1);
	}
	
	public String getPreviousHash() {
		return getLastBlock().getHash();
	}
	
	public void resetBlocks() {
		dao.deleteAllBlock(Peer.myPeer.getUserName());
		blocks = new ArrayList<Block>();
	}
	
	public void getBlocksFromDB() {
		ArrayList<Block> blocks = dao.getBlocks();
		BlockMaker blockMaker = new BlockMaker();
		if(blocks.size() != 0) setBlocks(blocks);
		else BlockChain.getBlocklist().applyBlock(blockMaker.makeGenesisBlock());
	}
}
