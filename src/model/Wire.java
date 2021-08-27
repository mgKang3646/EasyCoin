package model;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import encrypt.KeyFromPem;
import javafx.stage.Stage;
import util.PemFileChooser;

public class Wire {
	
	private PemFileChooser pemFileChooser;
	private KeyFromPem keyFromPem;
	private PublicKey publicKey;
	private String userName;
	private File file;

	
	public Wire() {
		pemFileChooser = new PemFileChooser();
		keyFromPem = new KeyFromPem();
	}
	
	public void settingPublicKey(Stage stage) {
		setPublicKeyPemFile(stage);
		if(file != null) {
			setPublicKey();
			if(publicKey != null) {
				setUserName();
			}
		}
	}
	
	private void setPublicKeyPemFile(Stage stage) {
			String title = "publicKey를 선택해주세요";
			file = pemFileChooser.getFileFromFileChooeser(stage,title);
	}
	
	private void setPublicKey() {
		try {
			publicKey = keyFromPem.readPublicKeyFromPemFile(file.getPath());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setUserName() {
		userName = keyFromPem.getUserName();
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public String getUserName() {
		return userName;
	}

	public File getFile() {
		return file;
	}
	
	public boolean isPublicKey() {
		if(publicKey != null) return true;
		else return false;
	}
	
	public boolean isFile() {
		if(file != null) return true;
		else return false;
	}
	
	public boolean isUserNameEqual(String myName) {
		if(userName.equals(myName)) return true;
		else return false;
	}
	
}
