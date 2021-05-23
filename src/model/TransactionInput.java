package model;

public class TransactionInput {

	String miner;
	String utxoHash;
	float inputValue;
	String transactionHash;
	public TransactionInput(String miner,String utxoHash,float inputValue) {
		this.miner = miner;
		this.utxoHash = utxoHash;
		this.inputValue = inputValue;
	}

	public float getInputValue() {
		return inputValue;
	}

	public void setInputValue(float inputValue) {
		this.inputValue = inputValue;
	}

	public String getMiner() {
		return miner;
	}

	public String getUtxoHash() {
		return utxoHash;
	}
	
	public void setTransactionHash(String hash) {
		this.transactionHash = hash;
	}
	
	public String getTransactionHash() {
		return this.transactionHash;
	}
}
