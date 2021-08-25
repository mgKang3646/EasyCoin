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
	
	public Block makeTmpBlock(JsonObject jsonObject) {
		block = new Block();
		block.setNum(jsonObject.getInt("blockNum"));
		block.setNonce(jsonObject.getInt("nonce"));
		block.setTimestamp(jsonObject.getString("timestamp"));
		block.setPreviousBlockHash(BlockChain.getBlocklist().getPreviousHash()); // 본인이 갖고 있는 마지막블록의 이전해쉬여야함.
		block.generateHash();
		
		return block;
	}
	
	public Block makeMinedBlock(int nonce, String timestamp) {
		block = new Block();
		block.setNum(BlockChain.getBlocklist().getBlockNum());
		block.setPreviousBlockHash(BlockChain.getBlocklist().getPreviousHash());
		block.setNonce(nonce);
		block.setTimestamp(timestamp);
		block.generateHash();
		
		return block;
	}

}
