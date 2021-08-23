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

public class KeyFromPem {
	
	String userName;
	
	public String getUserName() {
		return this.userName;
	}

	public PrivateKey readPrivateKeyFromPemFile(String privateKeyName) throws IOException, NoSuchAlgorithmException  {
		try { 
			String tmpPem = makeStringFromPem(privateKeyName); //  Pem파일 String 객체로 만들기
			String userName = findUserName(tmpPem); // userName 확보하기
			String pem = eliminateDeadCode(tmpPem, userName); // 불필요한 부분 제거하기
			
			byte[] decoded = Base64.decode(pem);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("ECDSA");
			PrivateKey privateKey = factory.generatePrivate(spec);
			
			return privateKey;
		
		}catch(InvalidKeySpecException e) {
			System.out.println("잘못된 개인키 생성");
			return null;
		}
	}
	
	public PublicKey readPublicKeyFromPemFile(String publicKeyName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		try {
			String tmpPem = makeStringFromPem(publicKeyName); //  Pem파일 String 객체로 만들기
			String userName = findUserName(tmpPem); // userName 확보하기
			String pem = eliminateDeadCode(tmpPem, userName); // 불필요한 부분 제거하기
			
			byte[] decoded = Base64.decode(pem);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("ECDSA");
			PublicKey publicKey = factory.generatePublic(spec);
			
			return publicKey;
		
		}catch(InvalidKeySpecException e) {
			System.out.println("잘못된 공개키 생성");
			return null;
		}
	}
	
	// 관심사 : Pem 파일 -> String 객체로 변환하기
	private String makeStringFromPem(String filename) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(filename)); 
		String line;
		String pem = "";
		
		while((line = br.readLine()) != null) {
			pem += line + "\n";
		}
		
		br.close();
		
		return pem;
	}
	
	// 관심사 : UserName 확보하기
	private String findUserName(String pem) throws IOException {
		
		BufferedReader br = new BufferedReader(new StringReader(pem));
		
		String line = br.readLine();
		line = line.replaceAll("-----BEGIN ", "");
		line = line.replaceAll("-----", "");
		
		this.userName = line;
		
		return line;
	}
	
	// 관심사 : PEM 파일 내에 불필요한 부분 제거하기
	private String eliminateDeadCode(String pem, String userName) throws IOException {
		
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
	}
	
	

}
