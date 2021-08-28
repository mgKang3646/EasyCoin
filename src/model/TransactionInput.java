package model;

import util.Encoding;

public class TransactionInput {
	
	private String miner;
	private String utxoHash;
	private float inputValue;
	private String itxoHash;
	private String transactionHash;
	
	public String getMiner() {
		return miner;
	}

	public String getUtxoHash() {
		return utxoHash;
	}

	public String getTransactionHash() {
		return transactionHash;
	}

	public float getInputValue() {
		return inputValue;
	}
	
	public String getItxoHash() {
		return itxoHash;
	}

	public void setMiner(String miner) {
		this.miner = miner;
	}

	public void setUtxoHash(String utxoHash) {
		this.utxoHash = utxoHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}

	public void setInputValue(float inputValue) {
		this.inputValue = inputValue;
	}
	
	public void setItxoHash(String itxoHash) {
		this.itxoHash = itxoHash;
	}
	
	public void generateItxoHash() {
		itxoHash = Encoding.getSHA256HexHash(miner + utxoHash + inputValue);
	}
}
