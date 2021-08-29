package model;

import java.security.PublicKey;

public class RewardTransaction {
	
	Transaction rewardTx;
	TransactionOutput rewardUTXO;
	TransactionInput rewardITXO;
	
	public Transaction getRewardTx() {
		return rewardTx;
	}

	public TransactionOutput getRewardUTXO() {
		return rewardUTXO;
	}

	public TransactionInput getRewardITXO() {
		return rewardITXO;
	}
	
	public void generateRewardObjects() {
		makeRewardUTXO();
		makeRewardITXO();
		makeRewardTx();
	}

	private void makeRewardTx() {
			PublicKey sender = Peer.myPeer.getPublicKey();
			PublicKey recipient = Peer.myPeer.getPublicKey();
			float value = 0;
			
			rewardTx = new Transaction(sender,recipient,value);
			rewardTx.generateHash();
			rewardTx.generateSignature(Peer.myPeer.getPrivateKey());
			rewardTx.getItxoList().add(rewardITXO);
	}
	
	private void makeRewardUTXO() {
		rewardUTXO = new TransactionOutput();
		rewardUTXO.setMiner(Peer.myPeer.getUserName());
		rewardUTXO.setRecipient(Peer.myPeer.getPublicKey());
		rewardUTXO.setValue(20f);
		rewardUTXO.generateUtxoHash();
	}
	
	private void makeRewardITXO() {
		rewardITXO = Wallet.itxo.makeITXO(rewardUTXO);
	}

}
