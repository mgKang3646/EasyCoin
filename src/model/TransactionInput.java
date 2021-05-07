package model;

public class TransactionInput {

	String miners;
	String utxoHash;
	String transactionHash;
	public TransactionInput(String miners,String utxoHash) {
		this.miners = miners;
		this.utxoHash = utxoHash;
	}

	public String getMiners() {
		return miners;
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
