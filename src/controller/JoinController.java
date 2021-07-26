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
		
		// ���ɻ� : userName ��ȿ�� �˻� ( ���� )
		if(userName == "") {
			userNameCheck.setText("�г����� �����Դϴ�.");
			userNameCheck.setVisible(true);
		}
		// ���ɻ� : userName �ߺ� �˻� �� DB ����
		else {
			try {
				// ���ɻ� : DB �ߺ� üũ 
				Dao dao = new Dao();
				int checkDuplicateResult = dao.checkDuplicateUserName(userName);
				
				// 1. �ߺ��� �ȵ� ���
				if( checkDuplicateResult > 0) { // ���ɻ� : userName �ߺ� �˻� 
					
					// ���ɻ� : �α��ν� �ʿ��� Pem ���� �����
					GeneratingKey generatingKey = new GeneratingKey();// ���ɻ� : ����Ű, ����Ű ����
					String localhost ="localhost:"+ (5500 + (int)(Math.random()*100));// ���ɻ� : �ּ� ����
					Pem pem = new Pem(userName); // ���ɻ� : Pem ���� �����
					pem.makePrivateAndPublicPemFile(generatingKey.getPrivateKey(), generatingKey.getPublicKey());
					
					// ���ɻ� : Peer ���� DB ����
					int joinResult = dao.join(localhost, userName);
					// ȸ������ DB ���� ������ ���
					if(joinResult > 0) {
							// ������ ��ȯ
							newPage = new NewPage("/view/login.fxml", stage);
							newPage.createPageOnCurrentStage();
							newPage = new NewPage("/view/popup.fxml", stage);
							Controller controller = newPage.getController();
							controller.setObject("ȸ�������� �Ϸ�Ǿ����ϴ�.");
							newPage.createPageOnNewStage();
					}
					// ȸ������ DB ���� ������ ���
					else {}	
					
				// 2. �ߺ��� ��� 
				}else if(checkDuplicateResult == 0) {
					userNameCheck.setText("�г����� �ߺ��˴ϴ�.");
					userNameCheck.setVisible(true);
				// 3. SQL�� ���� ���� �߻� 
				}else {}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ���ɻ� : �α��� �������� ���ư���
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


