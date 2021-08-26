package json;

import java.io.BufferedReader;

import javax.json.Json;
import javax.json.JsonObject;

import model.Block;
import model.BlockChain;
import model.BlockMaker;
import model.BlockVerify;
import model.Refresh;
import model.OtherPeer;
import model.PeerThread;

public class PeerThreadReceive{
	private JsonObject jsonObject;
	private Block tmpBlock;
	private BlockMaker blockMaker;
	private BlockVerify blockVerify;
	private JsonSend jsonSend;
	private PeerThread peerThread;
	private int blockNum;

	public PeerThreadReceive() {
		blockVerify = BlockChain.getBlockverify();
		blockMaker = new BlockMaker();
	}
	
	public void setPeerThread(PeerThread peerThread) {
		this.peerThread = peerThread;
	}
	
	public void read(BufferedReader bufferedReader) {
		setJsonObject(bufferedReader);
		processJsonQuery();
	}
	
	public int getBlockNum() {
		return blockNum;
	}
	
	private void setJsonObject(BufferedReader bufferedReader) {
		jsonObject = Json.createReader(bufferedReader).readObject();
	}

	private void processJsonQuery() {
		String key = jsonObject.getString("identifier");		
		switch(key) {
			case "minedBlock" : verifyBlock(); break;
			case "verifyResult" : handleVerifyResult(); break;
			case "requestBlockNum" : responseBlockNum(); break;
			default : break;
		}
	}
	
	private void verifyBlock() {
		tmpBlock = blockMaker.makeTmpBlock(jsonObject);
		tmpBlock.verifyBlock(jsonObject.getString("hash"));
		blockVerify.setTmpBlock(tmpBlock);
		blockVerify.setIsMinedBlock(false);
		blockVerify.doPoll(true); // 채굴자
		blockVerify.doPoll(tmpBlock.isValid()); // 검증자
		blockVerify.waitOtherPeerPoll();
		blockVerify.broadCastingVerifiedResult();
	}
	
	private void handleVerifyResult() {
		blockVerify.doPoll(jsonObject.getBoolean("verifyResult"));
		blockVerify.waitOtherPeerPoll();
	}
	
	private void responseBlockNum() {
		jsonSend = new JsonSend(peerThread);
		jsonSend.sendResponseBlockNum();
	}
}
