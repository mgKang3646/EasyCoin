package json;

import java.io.BufferedReader;

import javax.json.Json;
import javax.json.JsonObject;

import model.Block;
import model.BlockChain;
import model.Peer;

public class PeerThreadReceive implements JsonReceive{
	private Peer peer;
	private BlockChain blockchain;
	

	public PeerThreadReceive(Peer peer) {
		this.peer = peer;
		this.blockchain = peer.getBlockchain();
	}
	
	public JsonObject getJsonObject(BufferedReader br) {
		return Json.createReader(br).readObject();
	}

	public void processJsonQuery(JsonObject object) {
		String key = object.getString("identifier");
		
		switch(key) {
			case "minedBlock" : verifyBlock(object);// 1. ���� 2. ������������� 3. �ٸ� Peer ������� Ȯ�� 4. ������ ������ �����Ϸ�.
			default : break;
		}
	}
	
	private void verifyBlock(JsonObject object) {
		Block tmpBlock = new Block();
		String inputHash = object.getString("hash");

		tmpBlock.setNonce(object.getInt("nonce"));
		tmpBlock.setTimestamp(object.getString("timestamp"));
		tmpBlock.setPreviousBlockHash(object.getString("previousHash"));
		tmpBlock.generateHash();
		
		// �ٽ� ä���ϴ� ��� Null�� �ʱ�ȭ ���־�� ��
		if(inputHash.equals(tmpBlock.getHash())) {
			blockchain.setTmpBlock(tmpBlock);
			blockchain.setTmpBlockVerified(true);
		}else {
			blockchain.setTmpBlock(tmpBlock);
			blockchain.setTmpBlockVerified(false);
		}
	}
}
