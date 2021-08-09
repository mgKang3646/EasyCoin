package controller;

import java.io.IOException;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.Peer;

public interface Controller extends Initializable{
	
	public void setStage(Stage stage);
	public void setPeer(Peer peer);
	public void setObject(Object object);
	public void executeDefaultProcess() throws IOException;
	public void mainButtonAction()  throws IOException;
	public void subButtonAction()  throws IOException;
	public void mainThreadAction();

}
