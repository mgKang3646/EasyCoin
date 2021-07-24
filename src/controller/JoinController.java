package controller;

import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.GeneratingKey;
import encrypt.Pem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Peer;
import util.NewPage;

public class JoinController implements Controller {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	Stage stage;
	NewPage newPage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userNameCheck.setVisible(false);
		newPage = new NewPage();
	}
	
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
	}
	
	public void backLoginPage() {
		newPage.createPageOnCurrentStage("/view/login.fxml", stage);
	}
	
	// 관심사 : 새로운 Peer 회원 정보 생성 
	public void join() throws IOException{
		
		String userName = userNameText.getText();
		
		// 관심사 : userName 유효성 검사 ( 공백 )
		if(userName == "") {
			userNameCheck.setText("닉네임이 공백입니다.");
			userNameCheck.setVisible(true);
		}
		// 관심사 : userName 중복 검사 및 DB 삽입
		else {
			if(processDuplicateUserName(userName)) { // 관심사 : userName 중복 검사 
				
				GeneratingKey generatingKey = generateKey(userName); // 관심사 : 개인키, 공개키 생성
				String localhost = getLocalhost();	// 관심사 : 주소 생성
				makePemFile(generatingKey.getPrivateKey(),generatingKey.getPublicKey(),userName); // 관심사 : 개인키, 공개키 Pem 파일 생성
				
				// 관심사 : Peer 정보 DB 삽입
				if(insertIntoDB(localhost,userName)) {
					// 관심사 : DB 저장 성공 시, 회원가입 성공 팝업 생성 후 로그인 페이지로 자동이동.
					newPage.createPageOnCurrentStage("/view/login.fxml", stage); 
					newPage.createPageOnNewStage("/view/popup.fxml", stage);
				}else {
					//DB 삽입과정 중 문제 발생
				}	
			}
		}
	}
	
	// 관심사 : userName 중복 검사 유효성 결과 반환
	public boolean processDuplicateUserName(String userName) throws IOException {
		
		Dao dao = new Dao();
		int duplicateResult = dao.checkDuplicateUserName(userName);
		
		if(duplicateResult == 1) { // 중복 X
			return true;
		}else if(duplicateResult == 0) { //중복 O
			userNameCheck.setText("닉네임이 중복됩니다.");
			userNameCheck.setVisible(true);
			return false;
		}else { // SQL문 실행 문제 발생 
			return false;
		}
	}
	
	// 관심사 : 개인키, 공개키 생성
	public GeneratingKey generateKey(String userName) {
		// 관심사 : 개인키, 공개키 생성
		GeneratingKey generatingKey = new GeneratingKey();
		generatingKey.generateKeyPair();
		
		return generatingKey;
	}
	
	// 관심사 : 주소 생성
	public String getLocalhost() {
		return "localhost:"+ (5500 + (int)(Math.random()*100));
	}
	
	// 관심사 : 개인키, 공개키 Pem 파일 생성
	public void makePemFile(PrivateKey privateKey, PublicKey publicKey,String userName) throws IOException {
		
		Pem pem = new Pem(userName);
		pem.makePemFile(privateKey);
		pem.makePemFile(publicKey);
		
	}
	
	// 관심사 : Peer 정보 DB 삽입
	public boolean insertIntoDB(String localhost, String userName) {
		// 관심사 : DB 가입 정보 저장
		Dao dao = new Dao();
		int joinResult = dao.join(localhost, userName);
		if(joinResult > 0) {
			return true;
		}else {
			return false;
		}
	}		
}	


