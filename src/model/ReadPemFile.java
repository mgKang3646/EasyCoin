package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.encoders.Base64;

public class ReadPemFile {
	
	private String username = null;

	public PrivateKey readPrivateKeyFromPemFile(String privateKeyName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		String data = readString(privateKeyName);
		System.out.println("EC ����Ű�� "+ privateKeyName + "�κ��� �ҷ��Խ��ϴ�.");
		
		data = data.replaceAll(username, "");
		data = data.replaceAll("-----END -----","");
		
		System.out.println("data");
		System.out.println(data);
		
		byte[] decoded = Base64.decode(data);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
		KeyFactory factory = KeyFactory.getInstance("ECDSA");
		PrivateKey privateKey = factory.generatePrivate(spec);
		
		return privateKey;
	}
	
	public PublicKey readPublicKeyFromPemFile(String publicKeyName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String data = readString(publicKeyName);
		System.out.println("EC ����Ű�� "+ publicKeyName + "�κ��� �ҷ��Խ��ϴ�.");
		
		data = data.replaceAll(username, "");
		data = data.replaceAll("-----END -----", "");
		
		byte[] decoded = Base64.decode(data);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
		KeyFactory factory = KeyFactory.getInstance("ECDSA");
		PublicKey publicKey = factory.generatePublic(spec);
		
		return publicKey;
	}
	
	// ���̵� ���
	public String getUsername() {
		return username;
	}
	//Pem ���� �о���̱�
	private String readString(String filename) throws IOException {
		String pem ="";
		int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		
		while((line = br.readLine()) != null) {
			//ù��° �ٿ��� id ����
			if(count == 0) {
				line = line.replaceAll("-----BEGIN ","");
				line = line.replaceAll("-----", "");
				username = line; // ���̵� ����
			}
			
			pem += line + "\n";
			count++;
			
		}
		br.close();
		return pem;
	}
	
	

}
