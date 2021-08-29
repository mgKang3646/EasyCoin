package model;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import encrypt.Pem;
import encrypt.PemState;
import javafx.stage.Stage;
import json.JsonSend;
import util.P2PNet;

public class Wire {
	
	private PublicKey publicKey;
	private String userName;
	private JsonSend jsonSend;
	private File file;
	private Pem pem;

	public Wire() {
		pem = new Pem();
	}
	
	public PemState searchPublicKey(Stage stage) {
		String title = "publicKey를 선택해주세요";
		file = pem.getPemFileFromFileChooser(stage,title);
		if(file != null) {
			publicKey = pem.getPublicKey(file);
			if(publicKey != null) {
				userName = pem.getUserName(file);
				if(userName.equals(Peer.myPeer.getUserName())) return PemState.EQUALUSERNAME;
				else return PemState.NOTEQUALUSERNAME;
			}else return PemState.NONEKEY;
		}else return PemState.NONEFILE;
	}
	
	public boolean isAfford(float value) {
		if( value > Wallet.itxo.getBalance()) return false;
		else return true;
	}
	
	public Transaction makeTransaction(PublicKey recipient, float value) {
		PublicKey sender = Peer.myPeer.getPublicKey();
		PrivateKey pk = Peer.myPeer.getPrivateKey();
		
		Transaction newTransaction = new Transaction(sender,recipient,value);
		newTransaction.generateHash();
		newTransaction.generateSignature(pk);
		newTransaction.setItxoList(Wallet.itxo.getItxoForTx(value));
		
		return newTransaction;
	}
	
	public void broadcastingTX(Transaction tx) {
		jsonSend = new JsonSend(P2PNet.getServerListener());
		jsonSend.sendTransactionMessage(tx);
	}
		
	public PublicKey getPublicKey() {
		return publicKey;
	}

	public String getUserName() {
		return pem.getUserName(file);
	}

	public File getFile() {
		return file;
	}
}
