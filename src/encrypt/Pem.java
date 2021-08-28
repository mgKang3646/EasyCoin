package encrypt;

import java.io.File;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javafx.stage.Stage;

public class Pem {
	
	private GeneratingKey generatingKey;
	private PemFileChooser pemFileChooser;
	private WritingPem writingPem;
	private ReadingPem readingPem;
	private PemUtil pemUtil;
	
	public Pem() {
		readingPem = new ReadingPem();
		pemUtil = new PemUtil();
		generatingKey = new GeneratingKey();
		writingPem = new WritingPem();
		pemFileChooser= new PemFileChooser();
	}
	
	public void generateKey() {
		generatingKey.generateKeyPair();
	}
	
	public File getPemFileFromFileChooser(Stage stage,String title) {
		return pemFileChooser.getFileFromFileChooser(stage, title);
	}
	
	public PrivateKey getPrivateKey() {
		return generatingKey.getPrivateKey();
	}
	
	public PublicKey getPublicKey() {
		return generatingKey.getPublicKey();
	}
	
	public PrivateKey getPrivateKey(File file) {
		String pem = readingPem.makeStringFromPem(file.getPath());
		return pemUtil.getPrivateKey(pem);
	}
	
	public PublicKey getPublicKey(File file) {
		String pem = readingPem.makeStringFromPem(file.getPath());
		return pemUtil.getPublicKey(pem);
	}
	
	public String getUserName(File file) {
		String pem = readingPem.makeStringFromPem(file.getPath());
		return pemUtil.getUserName(pem);
	}
	
	public void writePemFile(Key key, String userName) {
		writingPem.writePemFile(key, userName);
	}
	
}
