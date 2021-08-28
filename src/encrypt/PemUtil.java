package encrypt;

import java.io.BufferedReader;
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

public class PemUtil {
	
	public PrivateKey getPrivateKey(String tmpPem)  {
		String pem = eliminateDeadCode(tmpPem,getUserName(tmpPem));
		return generatePrivateKey(pem);
	}
	
	public PublicKey getPublicKey(String tmpPem) {
		String pem = eliminateDeadCode(tmpPem, getUserName(tmpPem)); 
		return generatePublicKey(pem);
	}
	
	public String getUserName(String pem) {
		try {
			BufferedReader br = new BufferedReader(new StringReader(pem));
			String line = br.readLine();
			line = line.replaceAll("-----BEGIN ", "");
			line = line.replaceAll("-----", "");

			return line;
		} catch (IOException e) {
			System.out.println("pem파일에서 userName 추출 중 에러발생");
			return null;
		}
	}
		
	private String eliminateDeadCode(String pem, String userName)  {
		try {
			BufferedReader br = new BufferedReader(new StringReader(pem));
			String line;
			String result = "";
			while((line = br.readLine()) != null) {
				line = line.replaceAll(userName, "");
				line = line.replaceAll("-----END -----", "");
				line = line.replaceAll("-----BEGIN ", "");
				line = line.replaceAll("-----", "");	
				result += line+"\n";
			}
			return result;
		} catch (IOException e) {
			System.out.println("Pem파일 내 불필요한 코드 제거 중 에러발생");
			return null;
		}
	}
	
	private PrivateKey generatePrivateKey(String pem) {
		try {
			byte[] decoded = Base64.decode(pem);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("ECDSA");
			PrivateKey privateKey = factory.generatePrivate(spec);
			return privateKey;
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("개인키 생성 중 에러발생");
			return null;
		}
	}
	
	private PublicKey generatePublicKey(String pem) {
		try {
			byte[] decoded = Base64.decode(pem);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("ECDSA");
			PublicKey publicKey = factory.generatePublic(spec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("공개키 생성 중 에러발생");
			return null;
		}
		
	}
}
