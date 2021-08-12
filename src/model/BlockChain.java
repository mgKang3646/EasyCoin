package model;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

public class BlockChain {
	
	private ArrayList<Block> blocks;

	public BlockChain() {
		blocks = new ArrayList<Block>(); 
		blocks.add(createGenesisBlock());
	}
	
	public ArrayList<Block> getBlocks() { 
		return blocks; 
	}
	
	public ArrayList<Block> resetBlocks() {
		blocks = new ArrayList<Block>();
		return blocks;
	}
	
	public Block createGenesisBlock() {
		Block genesisBlock = new Block();
		
		genesisBlock.setPreviousBlockHash("0000000000000000000000000000000000000000000000000000000000000000");
		genesisBlock.setNonce("00000000");
		genesisBlock.setTimestamp("00000000");
		genesisBlock.setNum(0);
		genesisBlock.setHash(DigestUtils.sha256Hex(genesisBlock.getDataForHash()));
		
		return genesisBlock;
	}

}
