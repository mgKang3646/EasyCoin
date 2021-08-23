package model;

import database.Dao;
import encrypt.GeneratingKey;
import encrypt.Pem;

public class Join {
	
	private Dao dao;
	private Pem pem;
	private String userName;
	private GeneratingKey generatingKey;
	private boolean joinResult;
	
	public Join() {
		dao = new Dao();
		pem = new Pem();
		generatingKey = new GeneratingKey();
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean duplicateCheck() {
		if(dao.isUserNameExisted(userName)) return true;
		else return false;
	}
	
	public boolean doJoin() {
		makePemFile(); 
		storeDB();
		return joinResult;
	}
	
	private void makePemFile() {
		pem.setUserName(userName); 
		pem.makePrivateAndPublicPemFile(generatingKey.getPrivateKey(), generatingKey.getPublicKey());
	}
	
	private void storeDB() {
		if(dao.join(getLocalhost(), userName)) joinResult = true;
		else joinResult = false;
	}
	
	private String getLocalhost() {
		return "localhost:"+ (5500 + (int)(Math.random()*100));
	}
	
}
