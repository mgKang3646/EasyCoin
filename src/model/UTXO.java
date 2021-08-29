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
	
	public void deleteUTXO(String utxoHash) {
		System.out.println("ªË¡¶µ» UTXO");
		for(TransactionOutput utxo : utxoList) {
			if(utxo.getUtxoHash().equals(utxoHash)) {
				utxo.print();
				utxoList.remove(utxo);
				break;
			}
		}
	}
	
	public TransactionOutput makeUTXO(PublicKey recipient, float value) {
		TransactionOutput utxo = new TransactionOutput();
		utxo.setMiner(Peer.myPeer.getUserName());
		utxo.setRecipient(recipient);
		utxo.setValue(value);
		utxo.generateUtxoHash();
		return utxo;
	}
	
	public ArrayList<TransactionOutput> searchUTXO(PublicKey recipient) {
		searchedUTXOs = new ArrayList<TransactionOutput>();
		for(TransactionOutput utxo : utxoList) {
			if(utxo.getRecipient().toString().equals(recipient.toString())) searchedUTXOs.add(utxo);
		}
		return searchedUTXOs;
	}
}
