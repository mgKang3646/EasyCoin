package model;

import javax.json.JsonObject;

public class BlockMaker {
	
	Block block;
	
	public Block makeGenesisBlock() {
		block = new Block();
		block.setPreviousBlockHash("0000000000000000000000000000000000000000000000000000000000000000");
		block.setNonce(0);
		block.setTimestamp("00000000");
		block.setNum(0);
		block.generateHash();
		
		return block;
	}
	
	public Block makeTmpBlock(JsonObject jsonObject, String previousHash) {
		block = new Block();
		block.setNum(jsonObject.getInt("blockNum"));
		block.setNonce(jsonObject.getInt("nonce"));
		block.setTimestamp(jsonObject.getString("timestamp"));
		block.setPreviousBlockHash(previousHash); // 본인이 갖고 있는 마지막블록의 이전해쉬여야함.
		block.generateHash();
		
		return block;
	}
	
	public Block makeMinedBlock(BlockChain blockchain, int nonce, String timestamp) {
		block = new Block();
		block.setNum(blockchain.getBlockNum());
		block.setPreviousBlockHash(blockchain.getPreviousHash());
		block.setNonce(nonce);
		block.setTimestamp(timestamp);
		block.generateHash();
		
		return block;
	}

}
