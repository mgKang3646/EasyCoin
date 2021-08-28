package application;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.encoders.Base64;

import encrypt.GeneratingKey;
import util.Encoding;

public class Test {

	public void doTest() {
		
		GeneratingKey gk = new GeneratingKey();
		gk.generateKeyPair();
		
		PublicKey publicKey = gk.getPublicKey();
		System.out.println("인코딩 정 publicKey : " + publicKey.toString());
		
		try {
			String encodingPublicKey = Base64.toBase64String(publicKey.getEncoded());
			byte[] byteSender = Base64.decode(encodingPublicKey);
			X509EncodedKeySpec spec1 = new X509EncodedKeySpec(byteSender);
			KeyFactory factory1 = KeyFactory.getInstance("ECDSA","BC");
			PublicKey sender = factory1.generatePublic(spec1);
			System.out.println("인코딩 후 publickey : " + sender.toString());

		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		
	}
}
