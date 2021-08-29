package model;

import java.security.PublicKey;
import java.util.ArrayList;

import javax.json.JsonObject;
import javax.json.JsonValue;

import util.ECDSASignature;
import util.Encoding;
import util.ThreadUtil;

public class TxList {
	
	private ArrayList<Transaction> txList = new ArrayList<Transaction>();
	
	public ArrayList<Transaction> getTxList() {
		return txList;
	}
	
	public void resetTxList(){
		txList = new ArrayList<Transaction>(); 
		if(Peer.myPeer.getPublicKey() != null) {
			Wallet.rewardTransaction.generateRewardObjects();
			txList.add(Wallet.rewardTransaction.getRewardTx());
		}
	}
	
	public void addTxList(Transaction tx) {
		txList.add(tx);
	}
	
	public void processTxList(MiningState verifyResult) {
		if(verifyResult == MiningState.MININGGRANTED) {
			for(Transaction tx : txList) {
				processTx(tx);
			}
		}
	}
	
	public String getTxData() {
		StringBuilder sb = new StringBuilder();
		for(Transaction tx : txList) {
			sb.append(tx.toString()).append("\n");
		}
		return sb.toString();
	}
	
	public boolean processTx(Transaction tx) {
		float total = tx.getItxoSum();
		float value = tx.getValue();
		
		if(value > total) return false;	
		else {
			if(value != 0) {
				TransactionOutput valueUTXO = Wallet.utxo.makeUTXO(tx.getRecipient(), value);
				Wallet.utxo.addUTXO(valueUTXO);
			}
			TransactionOutput balanceUTXO = Wallet.utxo.makeUTXO(tx.getSender(), total - value);
			Wallet.utxo.addUTXO(balanceUTXO);
			requestDeleteUTXO(tx);
			return true;
		}	
	}
	
	public Transaction makeTransaction(JsonObject jsonObject) {
		
		PublicKey sender = Encoding.decodePublicKey(jsonObject.getString("sender"));
		PublicKey recipient = Encoding.decodePublicKey(jsonObject.getString("recipient"));
		float value = Float.parseFloat(jsonObject.getString("value"));
		String txHash = jsonObject.getString("txHash");
		byte[] signature = Encoding.decodeSinature(jsonObject.getString("signature"));
		
		Transaction tx = new Transaction(sender,recipient,value);
		tx.setSignature(signature);
		tx.setHash(txHash);

		
		for(int i = 0; i <jsonObject.getJsonArray("minerArr").size(); i++ ) {
			TransactionInput itxo = new TransactionInput();
			JsonValue minerJson = jsonObject.getJsonArray("minerArr").get(i);
			JsonValue utxoHashJson = jsonObject.getJsonArray("utxoHashArr").get(i);
			JsonValue inputValueJson = jsonObject.getJsonArray("inputValueArr").get(i);
			String miner = minerJson.toString().replaceAll("\"", "");
			String utxoHash = utxoHashJson.toString().replaceAll("\"", "");
			float inputValue = Float.parseFloat(inputValueJson.toString().replaceAll("\"", ""));
			
			itxo.setInputValue(inputValue);
			itxo.setMiner(miner);
			itxo.setUtxoHash(utxoHash);
			itxo.setTransactionHash(txHash);
			tx.getItxoList().add(itxo);
		}
		return tx;
	}
	
	public boolean verifyTransaction(Transaction tx) {
		String data = Encoding.encodeKey(tx.getSender())+Encoding.encodeKey(tx.getRecipient())+Float.toString(tx.getValue());
		return ECDSASignature.verifyECDSASig(tx.getSender(), data, tx.getSignature());
	}
	
	private void requestDeleteUTXO(Transaction tx) {
		for(TransactionInput itxo : tx.getItxoList()) {
			if(itxo.getMiner().equals(Peer.myPeer.getUserName())) Wallet.utxo.deleteUTXO(itxo.getUtxoHash());
			else Wallet.walletSend.requestDeleteUTXO(itxo);
			ThreadUtil.sleepThread(100);
		}
	}
}
