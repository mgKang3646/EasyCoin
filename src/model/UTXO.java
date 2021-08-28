package model;

import java.security.PublicKey;
import java.util.ArrayList;

import javax.json.JsonObject;

import util.Encoding;

public class UTXO {
	private  ArrayList<TransactionOutput> utxoList = new ArrayList<TransactionOutput>();
	private  ArrayList<TransactionOutput> searchedUTXOs;
	
	public ArrayList<TransactionOutput> getUtxoList(){
		return utxoList;
	}
	
	public void addUTXO(TransactionOutput transactionOutput) {
		utxoList.add(transactionOutput);
	}
	
	public void deleteUTXO() {
		
	}
	
	public ArrayList<TransactionOutput> searchUTXO(PublicKey recipient) {
		searchedUTXOs = new ArrayList<TransactionOutput>();
		for(TransactionOutput utxo : utxoList) {
			if(utxo.getRecipient().toString().equals(recipient.toString())) searchedUTXOs.add(utxo);
		}
		return searchedUTXOs;
	}

}
