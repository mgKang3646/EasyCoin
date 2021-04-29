package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PeerModel;
import model.PeerModel.Peer;

public class StateConnectionController implements Initializable {

	@FXML TableView<PeerRecord> connectTable;
	@FXML TableColumn<PeerRecord,String> connectUsername;
	@FXML TableColumn<PeerRecord,String> connectLocalhost;
	PeerModel peerModel;
	
	ObservableList<PeerRecord> list = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connectUsername.setStyle("-fx-alignment: CENTER");
		connectLocalhost.setStyle("-fx-alignment: CENTER");
	}
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
		
		for(Peer peer : peerModel.peerList) {
			list.add(new PeerRecord(peer.getUserName(),peer.getLocalhost()));
		}
		
		connectUsername.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("username"));
		connectLocalhost.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("localhost"));
		
		connectTable.setItems(list);
	
	}
	
	public class PeerRecord {
		
		private final SimpleStringProperty username;
		private final SimpleStringProperty localhost;
		
		public PeerRecord(String username, String localhost) {
			this.username = new SimpleStringProperty(username);
			this.localhost = new SimpleStringProperty(localhost);
		}

		public String getUsername() {
			return username.get();
		}

		public String getLocalhost() {
			return localhost.get();
		}

		public void setUsername(String username) {
			this.username.set(username);
		}

		public void setLocalhost(String localhost) {
			this.localhost.set(localhost);
		}
	}
	
	
}
