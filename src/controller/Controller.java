package controller;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

public interface Controller extends Initializable{
	
	public void setStage(Stage stage);
	public void setObject(Object object);
	public void mainButtonAction();
	public void subButtonAction();
	public void mainThreadAction();

}
