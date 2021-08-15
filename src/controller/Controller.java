package controller;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.Peer;

public interface Controller extends Initializable{
	public void setPeer(Peer peer);
	public void setStage(Stage stage);
	public void setObject(Object object);
	public void execute();
}
