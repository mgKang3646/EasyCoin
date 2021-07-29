package controller;

import java.io.IOException;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

public interface Controller extends Initializable{
	
	public void setStage(Stage stage);
	public void setObject(Object object);
	public void mainButtonAction()  throws IOException;
	public void subButtonAction()  throws IOException;
	public void mainThreadAction();

}
