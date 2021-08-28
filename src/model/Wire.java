package model;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import encrypt.Pem;
import encrypt.PemState;
import javafx.stage.Stage;
import newview.FxmlStage;

public class Wire {
	
	private PublicKey publicKey;
	private String userName;
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
