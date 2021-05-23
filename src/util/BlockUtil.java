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
	// ���ڼ��� ����
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
	
	// ���ڼ��� ����
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC"); // ECDSA ���� ���� ��ü ����
			ecdsaVerify.initVerify(publicKey); // PublicKey �ֱ� 
			ecdsaVerify.update(data.getBytes()); // �ŷ����� �ֱ�
			return ecdsaVerify.verify(signature); // ���� ���� : �߸��� PublicKey�̰ų� �ŷ������� ���ڼ���� �ٸ��� false ������ true ��ȯ
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
