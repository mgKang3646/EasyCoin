package model;

import java.util.ArrayList;



public class BlockchainModel {
	
	private ArrayList<Block> blocks = new ArrayList<Block>(); // ������ ArrayList�� ����
	public ArrayList<Block> getBlocks() { return blocks; }
	public ArrayList<Block> resetBlocks() {
		blocks = new ArrayList<Block>();
		return blocks;
	}
}
