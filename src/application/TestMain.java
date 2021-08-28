package application;

import java.security.Security;

public class TestMain {

	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		Test test = new Test();
		test.doTest();

	}

}
