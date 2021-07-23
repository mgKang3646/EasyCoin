package controller;

import java.io.IOException;
import java.net.URL;
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
import util.NewPage;

public class JoinController implements Controller {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userNameCheck.setVisible(false);
	}
	
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
	}
	
	public void backLoginPage() {
		NewPage newPage = new NewPage();
		newPage.createNewPage("/view/login.fxml", stage);
	}
	
	public void join() throws IOException{
		
		String userName = userNameText.getText();
		
		// 관심사 : userName 유효성 검사 ( 공백 )
		if(userName == "") {
			userNameCheck.setText("닉네임이 공백입니다.");
			userNameCheck.setVisible(true);
		}else {
			// 관심사 : userName 중복 검사 
			processDuplicateUserName(userName);
		}
		
	}
	
	public void processDuplicateUserName(String userName) throws IOException {
		
		Dao dao = new Dao();
		int duplicateResult = dao.checkDuplicateUserName(userName);
		
		if(duplicateResult == 1) { // 중복 X
			// 관심사 : 개인키, 공개키 생성
			GeneratingKey generatingKey = new GeneratingKey();
			generatingKey.generateKeyPair();
			
			// 관심사 : 개인키, 공개키 Pem 파일 생성
			Pem pem = new Pem(userName);
			pem.makePemFile(generatingKey.getPrivateKey());
			pem.makePemFile(generatingKey.getPublicKey());
			
			// 관심사 : DB 가입 정보 저장
			String localhost = "localhost:"+ (5500 + (int)(Math.random()*100)); // 주소 임의 설정
			int joinResult = dao.join(localhost, userName);
			
			if(joinResult > 0) {
				System.out.println("잘 저장됨");
			}else {
				System.out.println("잘 저장 안됨.");
			}
			
		}else if(duplicateResult == 0) { //중복 O
			userNameCheck.setText("닉네임이 중복됩니다.");
			userNameCheck.setVisible(true);
		}else { 
			// SQL문 실행 문제 발생 
		}
	}
	
	
	
	
	
	

}
