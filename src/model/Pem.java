package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class Pem {
	
	private PemObject pemObject;
	private File file;
	
	public Pem(Key key, String description) {
		this.pemObject = new PemObject(description, key.getEncoded());
	}
	
	public void write(String filename) throws IOException {
		file = new File("./pem/"+filename);
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(file))); // 현재 디렉토리에 있는 pem폴더안에 있는 파일
		try {
			pemWriter.writeObject(this.pemObject);
		} finally {
			pemWriter.close();
		}
	}
	
	public String getPath() {
		return file.getAbsolutePath();
	}
}
