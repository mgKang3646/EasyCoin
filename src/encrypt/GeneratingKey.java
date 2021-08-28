package encrypt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class GeneratingKey {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	//관심사 : 개인키 공유키 생성
	public void generateKeyPair() {
			// ? private key 생성 과정 분석 필요
		try {
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC"); // ECDSA 키생성 객체 생성
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); //PrivateKey 생성을 위한 랜덤객체 생성
				ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1"); // prime이 192bit로 표현, 2^192가지의 정수 중 하나가 PrivateKey가됨
		
				keyGen.initialize(ecSpec, random);  // privateKey는 2^192가지 정수 중 랜덤하게 선택된 prime이 되고 publickey는 prime 수만큼 ECC를 돌린 결과가 된다. 
		        KeyPair keyPair = keyGen.generateKeyPair(); // 키 생성 Prime만큼 돌리기.
		
		        privateKey = keyPair.getPrivate();
		        publicKey = keyPair.getPublic();
		        
		}catch(Exception e) {
				throw new RuntimeException(e);
		}
	}
	

}
