package model;

import java.util.ArrayList;



public class BlockchainModel {
	
	private ArrayList<Block> blocks = new ArrayList<Block>(); // 블럭들을 ArrayList에 저장
	public ArrayList<Block> getBlocks() { return blocks; }
	public ArrayList<Block> resetBlocks() {
		blocks = new ArrayList<Block>();
		return blocks;
	}
}
