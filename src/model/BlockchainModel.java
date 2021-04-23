package model;

import java.util.ArrayList;



public class BlockchainModel {
	
	private ArrayList<Block> blocks = new ArrayList<Block>(); // 블럭들을 ArrayList에 저장
	public ArrayList<Block> getBlocks() { return blocks; }
	public ArrayList<Block> resetBlocks() {
		blocks = new ArrayList<Block>();
		return blocks;
	}
	public String toString() {
		StringBuffer returnString = new StringBuffer();
		
		for(int i =0; i<blocks.size();i++) {
			returnString.append("+--------------------------------------------------------------------------------------|"+"\n");
			returnString.append(String.format("|"+"%-85s", "previousBlockHash: "+blocks.get(i).getPreviousBlockHash())+"|"+"\n");
			returnString.append(String.format("|"+"%-85s", "timestamp: "+blocks.get(i).getTimestamp())+"|"+"\n");
			returnString.append(String.format("|"+"%-85s", "nonce: "+blocks.get(i).getNonce())+"|"+"\n");
			returnString.append("+--------------------------------------------------------------------------------------+"+"\n");
			if(i==blocks.size()-1) {
				returnString.append(String.format("|"+"%-71s", "hash: "+blocks.get(i).getHash()+"|"+"\n"));
				returnString.append("+---------------------------------------------------------------------+"+"\n");
			}else {
				returnString.append(String.format("|"+"%-71s", "hash: "+blocks.get(i).getHash()+"| <-----------"+"\n"));
				returnString.append("+--------------------------------------------------------------------------                |"+"\n");
			}
		}
		return returnString.toString();
		
	}

}
