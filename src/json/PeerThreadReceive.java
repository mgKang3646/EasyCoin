package json;

import java.io.BufferedReader;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;

import model.Block;
import model.BlockChain;
import model.BlockMaker;
import model.BlockVerify;
import model.PeerThread;
import model.TransactionInput;
import model.TransactionOutput;
import model.Wallet;
import util.Encoding;

public class PeerThreadReceive{
	private JsonObject jsonObject;
	private Block tmpBlock;
	private BlockMaker blockMaker;
	private JsonSend jsonSend;
	private PeerThread peerThread;
	private int blockNum;

	public PeerThreadReceive() {
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
			case "requestITXO" : sendUTXO(); break;
			default : break;
		}
	}
	
	private void verifyBlock() {
		tmpBlock = blockMaker.makeTmpBlock(jsonObject);
		tmpBlock.verifyBlock(jsonObject.getString("hash"));
		BlockChain.getBlockVerify().verifyOtherMinedBlock(tmpBlock);
		BlockChain.getBlockChainSend().broadCastingVerifiedResult(tmpBlock);
	}
	
	private void handleVerifyResult() {
		BlockChain.getBlockVerify().handleVerifyResult(jsonObject.getBoolean("verifyResult"));
	}
	
	private void responseBlockNum() {
		jsonSend = new JsonSend(peerThread);
		jsonSend.sendResponseBlockNum();
	}
	
	private void sendUTXO() {
		PublicKey recipient = Encoding.decodePublicKey(jsonObject.getString("recipient"));
		ArrayList<TransactionOutput> utxos = Wallet.utxo.searchUTXO(recipient);
		if(utxos.size() != 0) {
			for(TransactionOutput utxo : utxos) {
				TransactionInput itxo = Wallet.itxo.makeITXO(utxo);
				jsonSend = new JsonSend(peerThread);
				jsonSend.sendResponseITXOMessage(itxo);
				sleepThread(350);
			}
		}
	}
	
	private void sleepThread(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
