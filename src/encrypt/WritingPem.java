package encrypt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class WritingPem {
	private PemObject pemObject;
	private File file;
	private String userName;
	private Key key;
	
	
	public void writePemFile(Key key, String userName) {
		this.key = key;
		this.userName = userName;
		createPemObject();
		makeFilePath();
		write();
	}
	
	private void createPemObject() {
		this.pemObject = new PemObject(userName,key.getEncoded());
	}
	
	private void makeFilePath() {
		if( key instanceof PrivateKey) {
			file = new File("./pem/"+userName+"PrivateKey.pem");
		}else if( key instanceof PublicKey) {
			file = new File("./pem/"+userName+"PublicKey.pem");
		}
	}
	
	private void write() {
		try {
			PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(file)));
			pemWriter.writeObject(this.pemObject);
			pemWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public String getPath() {
		return file.getAbsolutePath();
	}

}
