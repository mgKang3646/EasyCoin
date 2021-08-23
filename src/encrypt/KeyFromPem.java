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
			String tmpPem = makeStringFromPem(privateKeyName); //  Pem���� String ��ü�� �����
			String userName = findUserName(tmpPem); // userName Ȯ���ϱ�
			String pem = eliminateDeadCode(tmpPem, userName); // ���ʿ��� �κ� �����ϱ�
			
			byte[] decoded = Base64.decode(pem);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("ECDSA");
			PrivateKey privateKey = factory.generatePrivate(spec);
			
			return privateKey;
		
		}catch(InvalidKeySpecException e) {
			System.out.println("�߸��� ����Ű ����");
			return null;
		}
	}
	
	public PublicKey readPublicKeyFromPemFile(String publicKeyName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		try {
			String tmpPem = makeStringFromPem(publicKeyName); //  Pem���� String ��ü�� �����
			String userName = findUserName(tmpPem); // userName Ȯ���ϱ�
			String pem = eliminateDeadCode(tmpPem, userName); // ���ʿ��� �κ� �����ϱ�
			
			byte[] decoded = Base64.decode(pem);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("ECDSA");
			PublicKey publicKey = factory.generatePublic(spec);
			
			return publicKey;
		
		}catch(InvalidKeySpecException e) {
			System.out.println("�߸��� ����Ű ����");
			return null;
		}
	}
	
	// ���ɻ� : Pem ���� -> String ��ü�� ��ȯ�ϱ�
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
	
	// ���ɻ� : UserName Ȯ���ϱ�
	private String findUserName(String pem) throws IOException {
		
		BufferedReader br = new BufferedReader(new StringReader(pem));
		
		String line = br.readLine();
		line = line.replaceAll("-----BEGIN ", "");
		line = line.replaceAll("-----", "");
		
		this.userName = line;
		
		return line;
	}
	
	// ���ɻ� : PEM ���� ���� ���ʿ��� �κ� �����ϱ�
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
