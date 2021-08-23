package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Peer;

public class Dao {
	
	public boolean isUserNameExisted(String userName) {
		try {
			// ���ɻ� : Ŀ�ؼ� ��ü ����
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE WHERE username = ?";
			
			// ���ɻ� : SQL�� ���� ��ü ����
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userName);
			
			// ���ɻ� : userName �ߺ� üũ
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return true; // �ߺ��� userName�� ����
			}else {
				return false; // �ߺ��� �� ��.
			}
		} catch (SQLException e) {
			System.out.println("UserName �ߺ� üũ SQL�� ���� �� ���� �߻�");
			return false; // SQL�� ���� �� ���� �߻�
		}	
	}
	
	// ���ɻ� : ȸ������ ���� DB ����
	public boolean join(String localhost, String userName){
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "INSERT INTO PEERTABLE VALUES (?,?)";
			
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, localhost);
			pstmt.setString(2, userName);
			if(pstmt.executeUpdate() > 0 ) return true;
			else return false;
			
		} catch (SQLException e) {
			System.out.println("ȸ������ ���� DB ���� �� SQL ���� ���� �߻�");
			return false;
		}
	}
	
	// ���ɻ� : Peer ������ ���� ����
	public PeerDto getPeer(String userName) {
		PeerDto dto = new PeerDto();
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE WHERE username = ?";
			
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String username = rs.getString("username");
				String localhost = rs.getString("localhost");
				
				dto.setUserName(username);
				dto.setLocalhost(localhost);
			}
			return dto;
			
		} catch (SQLException e) {
			System.out.println("�α��� ��, Peer ��ü ���� ���� �� SQL ���� ���� �߻�");
			return null;
		}
	}
	
	// ���ɻ� : DB�� ����� ��ü PEER ���� ����
	public ArrayList<Peer> getPeers(String userName) {
		ArrayList<Peer> peers = new ArrayList<Peer>();

		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("username").equals(userName)) continue; // �ڱ� �ڽ��� ����
				
				Peer peer = new Peer();
				peer.setLocalhost(rs.getString("localhost"));
				peer.setUserName(rs.getString("username"));
				peers.add(peer);
			}
			return peers;
		} catch (SQLException e) {
			System.out.println("DB�� ����� ��ü Peer ���� �� SQL ���� ���� �߻�");
			return null;
		}
		
	}
	
	
	
	

}
