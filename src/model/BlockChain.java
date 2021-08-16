package model;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

public class BlockChain {
	
	private ArrayList<Block> blocks;
	private BlockVerify blockVerify;
	
	public BlockChain() {
		blocks = new ArrayList<Block>();
		blockVerify = new BlockVerify();
		blocks.add(createGenesisBlock());
	}
	
	public ArrayList<Block> getBlocks() { 
		return blocks; 
	}
	
	public void addBlock(Block block) {
		blocks.add(block);
	}
	
	public void addTmpBlock() {
		blocks.add(blockVerify.getTmpBlock());
	}
	
	public String getPreviousHash() {
		return getLastBlock().getHash();
	}
	
	public int getBlockNum() {
		return blocks.size();
	}
	
	public Block getLastBlock() {
		return blocks.get(blocks.size()-1);
	}
	
	public BlockVerify getBlockVerify() {
		return blockVerify;
	}
	
	public ArrayList<Block> resetBlocks() {
		blocks = new ArrayList<Block>();
		return blocks;
	}
	
	
	
	
	public Block createGenesisBlock() {
		Block genesisBlock = new Block();
		genesisBlock.setPreviousBlockHash("0000000000000000000000000000000000000000000000000000000000000000");
		genesisBlock.setNonce(0);
		genesisBlock.setTimestamp("00000000");
		genesisBlock.setNum(0);
		genesisBlock.generateHash();
		
		return genesisBlock;
	}

}
