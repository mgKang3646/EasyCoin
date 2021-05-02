package util;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;



public class BlockUtil {

	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	// 전자서명 생성
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}
	
	// 전자서명 검증
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC"); // ECDSA 서비스 제공 객체 생성
			ecdsaVerify.initVerify(publicKey); // PublicKey 넣기 
			ecdsaVerify.update(data.getBytes()); // 거래정보 넣기
			return ecdsaVerify.verify(signature); // 검증 시작 : 잘못된 PublicKey이거나 거래정보가 전자서명과 다르면 false 같으면 true 반환
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
