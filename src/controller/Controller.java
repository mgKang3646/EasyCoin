package controller;

import javafx.fxml.Initializable;
import model.Peer;

public interface Controller extends Initializable{
	public void setPeer(Peer peer);
	public void setObject(Object object);
	public void execute();
}
