package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Peer;

public class Dao {
	
	// ���ɻ� : UserName �ߺ� üũ
	public int checkDuplicateUserName(String userName) {
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
				return 0; // �ߺ��� userName�� ����
			}else {
				return 1; // �ߺ��� �� ��.
			}
		} catch (SQLException e) {
			System.out.println("UserName �ߺ� üũ SQL�� ���� �� ���� �߻�");
			return -1; // SQL�� ���� �� ���� �߻�
		}	
	}
	
	// ���ɻ� : ȸ������ ���� DB ����
	public int join(String localhost, String userName){
		
		int result=0;
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "INSERT INTO PEERTABLE VALUES (?,?)";
			
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, localhost);
			pstmt.setString(2, userName);
			
			result = pstmt.executeUpdate();
			
			return result;
		} catch (SQLException e) {
			System.out.println("ȸ������ ���� DB ���� �� SQL ���� ���� �߻�");
			return result;
		}
	}
	
	// ���ɻ� : Peer ������ ���� ����
	public Peer getPeer(String userName) {
		Peer peer = new Peer();
		
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
				
				peer.setUserName(username);
				peer.setLocalhost(localhost);
			}
			
			return peer;
			
		} catch (SQLException e) {
			System.out.println("�α��� ��, Peer ��ü ���� ���� �� SQL ���� ���� �߻�");
			return null;
		}
	}
	
	// ���ɻ� : DB�� ����� ��ü PEER ���� ����
	public ArrayList<Peer> getPeers() {
		ArrayList<Peer> peers = new ArrayList<Peer>();

		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
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
