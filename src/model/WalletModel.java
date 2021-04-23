package model;

public class WalletModel {
	
	private String userLocalHost;
	private String privateKey;
	private String publicKey;
	private String username;
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getUserLocalHost() {
		return userLocalHost;
	}

	public void setUserLocalHost(String userLocalHost) {
		this.userLocalHost = userLocalHost;
	}
}
