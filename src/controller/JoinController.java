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
import model.NewPage;

public class JoinController implements Controller  {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	Stage stage;
	NewPage newPage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
	}
	@Override
	public void setObject(Object object) {}
	@Override
	public void mainButtonAction()  {
		String userName = userNameText.getText();
		
		// 관심사 : userName 유효성 검사 ( 공백 )
		if(userName == "") {
			userNameCheck.setText("닉네임이 공백입니다.");
			userNameCheck.setVisible(true);
		}
		// 관심사 : userName 중복 검사 및 DB 삽입
		else {
			try {
				// 관심사 : DB 중복 체크 
				Dao dao = new Dao();
				int checkDuplicateResult = dao.checkDuplicateUserName(userName);
				
				// 1. 중복이 안된 경우
				if( checkDuplicateResult > 0) { // 관심사 : userName 중복 검사 
					
					// 관심사 : 로그인시 필요한 Pem 파일 만들기
					GeneratingKey generatingKey = new GeneratingKey();// 관심사 : 개인키, 공개키 생성
					String localhost ="localhost:"+ (5500 + (int)(Math.random()*100));// 관심사 : 주소 생성
					Pem pem = new Pem(userName); // 관심사 : Pem 파일 만들기
					pem.makePrivateAndPublicPemFile(generatingKey.getPrivateKey(), generatingKey.getPublicKey());
					
					// 관심사 : Peer 정보 DB 저장
					int joinResult = dao.join(localhost, userName);
					// 회원정보 DB 저장 성공한 경우
					if(joinResult > 0) {
							// 페이지 전환
							newPage = new NewPage("/view/login.fxml", stage);
							newPage.createPageOnCurrentStage();
							newPage = new NewPage("/view/popup.fxml", stage);
							Controller controller = newPage.getController();
							controller.setObject("회원가입이 완료되었습니다.");
							newPage.createPageOnNewStage();
					}
					// 회원정보 DB 저장 실패한 경우
					else {}	
					
				// 2. 중복된 경우 
				}else if(checkDuplicateResult == 0) {
					userNameCheck.setText("닉네임이 중복됩니다.");
					userNameCheck.setVisible(true);
				// 3. SQL문 실행 문제 발생 
				}else {}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 관심사 : 로그인 페이지로 돌아가기
	@Override
	public void subButtonAction() {
		try {
			this.newPage = new NewPage("/view/login.fxml", stage);
			newPage.createPageOnCurrentStage();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void mainThreadAction() {}

		
}	


