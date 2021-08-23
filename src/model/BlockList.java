package model;

import java.util.ArrayList;

public class BlockList {
	
	private ArrayList<Block> blocks;
	
	public BlockList() {
		blocks = new ArrayList<Block>();
	}
	
	public ArrayList<Block> getBlocks() { 
		return blocks; 
	}
	
	public void addBlock(Block block) {
		blocks.add(block);
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
	
	public ArrayList<Block> resetBlocks() {
		blocks = new ArrayList<Block>();
		return blocks;
	}
}
