package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.Block;
import model.BlockMaker;
import model.OtherPeer;
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
	public ArrayList<OtherPeer> getPeers(String userName) {
		ArrayList<OtherPeer> otherPeers = new ArrayList<OtherPeer>();

		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("username").equals(userName)) continue; // �ڱ� �ڽ��� ����
				
				OtherPeer otherPeer = new OtherPeer();
				otherPeer.setLocalhost(rs.getString("localhost"));
				otherPeer.setUserName(rs.getString("username"));
				otherPeers.add(otherPeer);
			}
			return otherPeers;
		} catch (SQLException e) {
			System.out.println("DB�� ����� ��ü Peer ���� �� SQL ���� ���� �߻�");
			return null;
		}
	}
	
	public void storeBlock(Block block) {
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String SQL = "INSERT INTO BLOCKTABLE VALUES( ?, ?,?, ?, ?, ?,?)";
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, block.getNum()+"");
			pstmt.setString(2, block.getNonce()+"");
			pstmt.setString(3, block.getTimestamp());
			pstmt.setString(4, "�ŷ�����");
			pstmt.setString(5, block.getPreviousBlockHash());
			pstmt.setString(6, block.getHash());
			pstmt.setString(7, Peer.myPeer.getUserName());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("DB �� ���� ����!");
			e.printStackTrace();
		}
	}
	
	public ArrayList<Block> getBlocks() {
		String SQL = "SELECT * FROM BLOCKTABLE WHERE username = ?";
		ArrayList<Block> blocks = new ArrayList<Block>();
		BlockMaker blockMaker = new BlockMaker();
		Block block;
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, Peer.myPeer.getUserName());
			blocks.sort(null);
			ResultSet rs = pstmt.executeQuery();
			// ����Ʈ �����ϱ� 
			while(rs.next()) {
				block = blockMaker.makeDBBlock(rs);
				blocks.add(block);
			}
			return blocks;
		} catch (SQLException e) {
			System.out.println("DB���� �� �������� �� ���� �߻�!");
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteAllBlock(String username) {
		String SQL = "DELETE FROM BLOCKTABLE WHERE username = ?";
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, username);
			int result = pstmt.executeUpdate();
			System.out.println("DB���� �� " + result+"�� ����");
			
		} catch (SQLException e) {
			System.out.println("�� ���� ���� �� SQL ���� ���� �߻�");
		}
	}
}
