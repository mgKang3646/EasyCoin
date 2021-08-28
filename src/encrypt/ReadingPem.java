package encrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.encoders.Base64;

public class ReadingPem {
	
	public String makeStringFromPem(String filename)  {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			String pem = "";
			while((line = br.readLine()) != null) {
				pem += line + "\n";
			}
			br.close();
			return pem;
		} catch (IOException e) {
			System.out.println("Pem 파일 리딩 중 에러발생");
			return null;
		}
	}
}
