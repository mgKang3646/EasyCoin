package database;

public class UserDTO {

	private String userLocalHost;
	private String privateKey;
	private String publicKey;
	

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
