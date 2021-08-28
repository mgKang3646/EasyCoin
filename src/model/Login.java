package model;

import java.io.File;
import java.security.PrivateKey;
import encrypt.Pem;
import encrypt.PemState;
import newview.FxmlStage;

public class Login {
	private Pem pem;
	private PrivateKey privateKey;
	private File file;
	
	public Login() {
		pem = new Pem();
	}
	
	public PemState doLogin() {
		String title = "로그인 할 개인키 PEM 파일을 선택하세요.";
		file = pem.getPemFileFromFileChooser(FxmlStage.getPrimaryStage(),title);
		if(file != null) {
			privateKey = pem.getPrivateKey(file);
			if(privateKey != null) return PemState.KEYEXISTED;
			else return PemState.NONEKEY;
		}else return PemState.NONEFILE;
	}
	
	public String getUserName() {
		return pem.getUserName(file);
	}
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	public File getFile() {
		return file;
	}
}
