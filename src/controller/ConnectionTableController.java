package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.OtherPeer;
import model.Peer;


public class ConnectionTableController implements Controller {
	
	@FXML TableView<PeerRecord> connectTable;
	@FXML TableColumn<PeerRecord,String> usernameColumn;
	@FXML TableColumn<PeerRecord,String> localhostColumn;
	//@FXML TableColumn<PeerRecord,String> leaderColumn;
	
	ObservableList<PeerRecord> list;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameColumn.setStyle("-fx-alignment: CENTER");
		localhostColumn.setStyle("-fx-alignment: CENTER");
		//leaderColumn.setStyle("-fx-alignment: CENTER");
	}

	@Override
	public void throwObject(Object object) {
	}

	@Override
	public void execute() {
		setPeer();
	}
	
	public void setPeer() {
		list = FXCollections.observableArrayList();
		PeerRecord myPeerRecord = new PeerRecord();
		myPeerRecord.setUsername(Peer.myPeer.getUserName());
		myPeerRecord.setLocalhost(Peer.myPeer.getLocalhost());
		list.add(myPeerRecord);
		
		for(OtherPeer otherPeer : Peer.peerList.getPeerList()) {
			PeerRecord otherPeerRecord = new PeerRecord();
			otherPeerRecord.setUsername(otherPeer.getUserName());
			otherPeerRecord.setLocalhost(otherPeer.getLocalhost());
			list.add(otherPeerRecord);
		}
		
		usernameColumn.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("username"));
		localhostColumn.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("localhost"));
		//leaderColumn.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("leader"));
		
		connectTable.setItems(list);
	
	}
	
	public class PeerRecord {
		
		private SimpleStringProperty username;
		private SimpleStringProperty localhost;
		private SimpleStringProperty leader;
		
		public String getLeader() {
			return leader.get();
		}
		
		public String getUsername() {
			return username.get();
		}

		public String getLocalhost() {
			return localhost.get();
		}

		public void setUsername(String username) {
			this.username = new SimpleStringProperty(username);
		}

		public void setLocalhost(String localhost) {
			this.localhost = new SimpleStringProperty(localhost);
		}
		
		public void setLeader(String leader) {
			this.leader = new SimpleStringProperty(leader);
		}
	}
}
