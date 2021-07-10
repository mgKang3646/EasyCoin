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
	@FXML TableColumn<PeerRecord,String> usernameColumn;
	@FXML TableColumn<PeerRecord,String> localhostColumn;
	@FXML TableColumn<PeerRecord,String> leaderColumn;
	
	PeerModel peerModel;
	
	ObservableList<PeerRecord> list = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameColumn.setStyle("-fx-alignment: CENTER");
		localhostColumn.setStyle("-fx-alignment: CENTER");
		leaderColumn.setStyle("-fx-alignment: CENTER");
	}
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
		
		// 본인 정보 테이블에 넣기
		if(peerModel.amILeader) {
			list.add(new PeerRecord(peerModel.walletModel.getUsername(),peerModel.walletModel.getUserLocalHost(),"리더"));
		}else {
			list.add(new PeerRecord(peerModel.walletModel.getUsername(),peerModel.walletModel.getUserLocalHost(),""));
		}
		// 다른 Peer 정보 테이블에 넣기
		for(Peer peer : peerModel.peerList) {
			if(peer.isLeader()) {
				list.add(new PeerRecord(peer.getUserName(),peer.getLocalhost(),"리더"));
			}else {
				list.add(new PeerRecord(peer.getUserName(),peer.getLocalhost(),""));
			}
		}
		
		usernameColumn.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("username"));
		localhostColumn.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("localhost"));
		leaderColumn.setCellValueFactory(new PropertyValueFactory<PeerRecord,String>("leader"));
		
		connectTable.setItems(list);
	
	}
	
	public class PeerRecord {
		
		private final SimpleStringProperty username;
		private final SimpleStringProperty localhost;
		private final SimpleStringProperty leader;
		
		public PeerRecord(String username, String localhost,String leader) {
			this.username = new SimpleStringProperty(username);
			this.localhost = new SimpleStringProperty(localhost);
			this.leader = new SimpleStringProperty(leader);
		}
		
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
			this.username.set(username);
		}

		public void setLocalhost(String localhost) {
			this.localhost.set(localhost);
		}
		
		public void setLeader(String leader) {
			this.leader.set(leader);
		}
	}
	
	
}
