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

public class Pem {
	
	private PemObject pemObject;
	private File file;
	private String userName;
	
	public Pem(String userName) {
		this.userName = userName;
	}
	
	public void makePemFile(Key key) throws IOException {
		
		this.pemObject = new PemObject(userName,key.getEncoded());

		if( key instanceof PrivateKey) {
			file = new File("./pem/"+userName+"PrivateKey.pem");
		}else if( key instanceof PublicKey) {
			file = new File("./pem/"+userName+"PublicKey.pem");
		}
		
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(file))); 
		
		try {
			pemWriter.writeObject(this.pemObject);
		} finally {
			pemWriter.close();
		}
	}
	
	public void makePrivateAndPublicPemFile(PrivateKey privateKey, PublicKey publicKey) {
		try {
			
			makePemFile(privateKey);
			makePemFile(publicKey);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPath() {
		return file.getAbsolutePath();
	}
}
