package model;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;

import database.Dao;
import database.PeerDto;
import encrypt.KeyFromPem;
import newview.FxmlStage;
import util.PemFileChooser;

public class Login {
	
	private Dao dao;
	private PemFileChooser pemFileChooser;
	private KeyFromPem keyFromPem;
	private PrivateKey privateKey;
	private File file;
	
	public Login() {
		pemFileChooser = new PemFileChooser();
		keyFromPem = new KeyFromPem();
		dao = new Dao();
	}
	
	public void doLogin() {
		setPemFilePath();
		setPrivateKeyFromPem();
		setMyPeer();
		setBlockChain();
	}
	
	public boolean isGetFile() {
		if(file == null) return false;
		else return true;
	}
	
	public boolean isGetPrivateKey() {
		if(privateKey == null) return false;
		else return true;
	}
	
	private void setPemFilePath() {
		String title = "로그인 할 개인키 PEM 파일을 선택하세요.";
		file = pemFileChooser.getFileFromFileChooeser(FxmlStage.getPrimaryStage(),title);
	}
	
	private void setPrivateKeyFromPem() {
		if(file != null) {
			try {
				privateKey = keyFromPem.readPrivateKeyFromPemFile(file.getPath());
			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setMyPeer() {
		if(privateKey != null) {
			String userName = keyFromPem.getUserName();
			PeerDto peerDto = dao.getPeer(userName);
			
			Peer.myPeer.setUserName(peerDto.getUserName());
			Peer.myPeer.setLocalhost(peerDto.getLocalhost());
			Peer.myPeer.setPrivateKey(privateKey);
		}
	}
	
	private void setBlockChain() {
		if(privateKey != null) {
			ArrayList<Block> blocks = dao.getBlocks();
			BlockMaker blockMaker = new BlockMaker();
			if(blocks.size() != 0) BlockChain.getBlocklist().setBlocks(blocks);
			else BlockChain.getBlocklist().applyBlock(blockMaker.makeGenesisBlock());
		}
	}
}
