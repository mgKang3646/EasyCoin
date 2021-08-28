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
	
	//���ɻ� : ����Ű ����Ű ����
	public void generateKeyPair() {
			// ? private key ���� ���� �м� �ʿ�
		try {
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC"); // ECDSA Ű���� ��ü ����
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); //PrivateKey ������ ���� ������ü ����
				ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1"); // prime�� 192bit�� ǥ��, 2^192������ ���� �� �ϳ��� PrivateKey����
		
				keyGen.initialize(ecSpec, random);  // privateKey�� 2^192���� ���� �� �����ϰ� ���õ� prime�� �ǰ� publickey�� prime ����ŭ ECC�� ���� ����� �ȴ�. 
		        KeyPair keyPair = keyGen.generateKeyPair(); // Ű ���� Prime��ŭ ������.
		
		        privateKey = keyPair.getPrivate();
		        publicKey = keyPair.getPublic();
		        
		}catch(Exception e) {
				throw new RuntimeException(e);
		}
	}
	

}
