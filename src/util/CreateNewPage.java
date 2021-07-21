package util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateNewPage {
	
	public void createNewPage(String url, Stage stageValue) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(url));
			Scene scene = new Scene(parent);
			Stage stage = stageValue;	
			stage.setScene(scene);
			
		} catch (IOException e) {
			System.out.println("새로운 페이지 생성 중 에러 발생");
		}	
	}

}
