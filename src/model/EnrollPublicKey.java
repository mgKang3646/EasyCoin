package model;

import java.io.File;
import java.security.PublicKey;

import encrypt.Pem;
import encrypt.PemState;
import javafx.stage.Stage;

public class EnrollPublicKey {
	
	private File file;
	private Pem pem;
	private String userName;
	private PublicKey publicKey;
	 
	public EnrollPublicKey(){
		pem = new Pem();
	}
	
	public PemState enrollPublicKey(Stage stage) {
		String title = "계좌를 선택해주세요.";
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
	
	public String getUserName() {
		return userName;
	}
	
	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	public void addRewardTx() {
		Wallet.rewardTransaction.generateRewardObjects();
		Transaction rewardTx = Wallet.rewardTransaction.getRewardTx();
		Wallet.txList.addTxList(rewardTx);
	}

}
