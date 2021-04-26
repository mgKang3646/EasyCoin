package model;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction {
	
	private PublicKey from;
	private PublicKey to;
	private float value;
	public byte[] signature;

	
	public Transaction(PublicKey from, PublicKey to, float value) {
		this.from = from ;
		this.to = to;
		this.value = value;
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = BlockUtil.getStringFromKey(from)+BlockUtil.getStringFromKey(to)+Float.toString(value);
		signature = BlockUtil.applyECDSASig(privateKey, data);// �ŷ� data�� ���� �ñ״�ó�� �����Ǳ� ������ data�� �ٲ�� �ñ״��İ� �޶�����. 
	}

}
