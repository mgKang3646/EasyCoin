package application;

import java.security.Security;

public class TestMain {

	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		Test test = new Test();
		test.doTest();

	}

}
