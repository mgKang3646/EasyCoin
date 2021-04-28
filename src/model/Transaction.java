package model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


public class Transaction {
	
	public PublicKey sender;
	public PublicKey recipient;
	public float value;
	public byte[] signature;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	
	public Transaction(PublicKey sender, PublicKey recipient, float value) {
		this.sender = sender ;
		this.recipient = recipient;
		this.value = value;
	}
	
	// �������
	public boolean processTransaction() {
			return false;
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = BlockUtil.getStringFromKey(sender)+BlockUtil.getStringFromKey(recipient)+Float.toString(value);
		signature = BlockUtil.applyECDSASig(privateKey, data);// �ŷ� data�� ���� �ñ״�ó�� �����Ǳ� ������ data�� �ٲ�� �ñ״��İ� �޶�����. 
	}
	
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	public boolean verifySignature() {
		String data = BlockUtil.getStringFromKey(sender)+BlockUtil.getStringFromKey(recipient)+Float.toString(value);
		return BlockUtil.verifyECDSASig(sender, data, signature);
	}

}
