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
		signature = BlockUtil.applyECDSASig(privateKey, data);// 거래 data를 통해 시그니처가 생성되기 떄문에 data가 바뀌면 시그니쳐가 달라진다. 
	}

}
