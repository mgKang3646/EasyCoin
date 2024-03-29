package model;

import java.util.ArrayList;

import javax.json.JsonObject;

public class ITXO {
	
	public ArrayList<TransactionInput> itxoList = new ArrayList<TransactionInput>();
	
	public void addITXO(TransactionInput itxo) {
		itxoList.add(itxo);
	}
	
	public void resetItxoList() {
		if(Peer.myPeer.getPublicKey() != null) {
			itxoList = new ArrayList<TransactionInput>();
			Wallet.walletSend.requestITXO();
			addUtxoToItxoList();
		}
	}
	
	public void addUtxoToItxoList() {
		for(TransactionOutput utxo : Wallet.utxo.searchUTXO(Peer.myPeer.getPublicKey())) {
			itxoList.add(makeITXO(utxo));
		}
	}
	
	public TransactionInput makeITXO(TransactionOutput utxo) {
		TransactionInput itxo = new TransactionInput();
		itxo.setMiner(utxo.getMiner());
		itxo.setInputValue(utxo.getValue());
		itxo.setUtxoHash(utxo.getUtxoHash());
		itxo.generateItxoHash();
		return itxo;
	}
	
	public TransactionInput makeITXO(JsonObject jsonObject) {
		TransactionInput itxo = new TransactionInput();
		itxo.setMiner(jsonObject.getString("miner"));
		itxo.setInputValue(Float.parseFloat(jsonObject.getString("inputValue")));
		itxo.setUtxoHash(jsonObject.getString("utxoHash"));
		itxo.setItxoHash(jsonObject.getString("itxoHash"));
		return itxo;
	}
	
	public boolean verifyITXO(JsonObject jsonObject) {
		String inputItxoHash = jsonObject.getString("itxoHash");
		TransactionInput itxo = new TransactionInput();
		itxo.setMiner(jsonObject.getString("miner"));
		itxo.setInputValue(Float.parseFloat(jsonObject.getString("inputValue")));
		itxo.setUtxoHash(jsonObject.getString("utxoHash"));
		itxo.generateItxoHash();
		
		if(itxo.getItxoHash().equals(inputItxoHash)) return true;
		else return false;
	}
	
	public ArrayList<TransactionInput> getItxoForTx(float value){
		ArrayList<TransactionInput> itxos = new ArrayList<TransactionInput>();
		float sum = 0;
		System.out.println("Ʈ����� ������ itxo��");
		for( TransactionInput itxo : itxoList ) {
			itxo.print();
			if( sum < value) {
				sum += itxo.getInputValue();
				itxos.add(itxo);
			}else break;
		}
		return itxos;
	}
	
	public float getBalance() {
		float sum = 0;
		System.out.println("�ܾ� itxo");
		for(TransactionInput itxo : itxoList) {
			itxo.print();
			sum += itxo.getInputValue();
		}
		return sum;
	}

}
