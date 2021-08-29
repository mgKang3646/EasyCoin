package util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Base64;

public class Encoding {
	
	public static String encodeKey(Key key) {
		return Base64.toBase64String(key.getEncoded());
	}
	
	public static String encodeSignature(byte[] signature) {
		return Base64.toBase64String(signature);
	}
	
	public static byte[] decodeSinature(String signature) {
		return Base64.decode(signature);
	}
	
	public static String getSHA256HexHash(String data) {
		return DigestUtils.sha256Hex(data);
	}
	
	public static PublicKey decodePublicKey(String publicKeyEncoded) {
		try {
			byte[] byteSender = Base64.decode(publicKeyEncoded);
			X509EncodedKeySpec spec1 = new X509EncodedKeySpec(byteSender);
			KeyFactory factory1 = KeyFactory.getInstance("ECDSA","BC");
			PublicKey publicKey = factory1.generatePublic(spec1);
			return publicKey;

		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			System.out.println("공유키 디코딩 과정 중 문제발생");
			return null;
		}
	}

}
