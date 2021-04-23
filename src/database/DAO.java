package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import model.Block;
import model.WalletModel;

public class DAO {
	
	// ���� �� DB ����
	public int join(String userLocalHost, String userPrivateKey, String userPublicKey, String username) {
		String SQL = "INSERT INTO PEERTABLE VALUES(?,?,?,?)";
		
		try {
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userPrivateKey);
			pstmt.setString(2, userPublicKey);
			pstmt.setString(3, userLocalHost);
			pstmt.setString(4, username);


			return pstmt.executeUpdate(); // SQL�� �����ؼ� �������θ� ��ȯ ������ ������ ������ ������ ��ȯ
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("DAO DB SQL ���԰������� ���� �߻�");
			e.printStackTrace();
		}
		
		return -1; // ������ �߻��ϴ� ���
	}
	
	// P2P ��Ʈ��ũ ������ ���� DB �� host �ּ� 
	public ArrayList<String> getPeersLocalhost() {
		ArrayList<String> peers = new ArrayList<String>(); // DB�ȿ� �ִ� PEER�� �ּҸ� ���� ����Ʈ

		try {
			
			String SQL = "SELECT localhost From PEERTABLE";
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) { // ResultSet�� �Ϲ����� set�� ���� �ٸ���.
				peers.add(rs.getString("localhost"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return peers;
	}
	
	// Ư�� ����Ű�� �ش��ϴ� ������ ���� ����
	public WalletModel getPeer(String privateKey){
		WalletModel walletModel = new WalletModel();

		try {
			
			String SQL = "SELECT * FROM PEERTABLE WHERE privatekey='"+privateKey+"';";
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) { // ResultSet�� �Ϲ����� set�� ���� �ٸ���.
				walletModel.setPrivateKey(rs.getString("privatekey"));
				walletModel.setPublicKey(rs.getString("publickey"));
				walletModel.setUserLocalHost(rs.getString("localhost"));
				walletModel.setUsername(rs.getString("username"));
			}
			
			return walletModel;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//ȣ��Ʈ �ּ� �ߺ�üũ
	public int registerCheck(String username) {
		
		try {
			String SQL ="SELECT * FROM PEERTABLE WHERE username = ?";
			
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return 1; // �ߺ��Ǵ� ���� ����
			}else {
				return 0; // �ߺ��Ǵ� ���� ����
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("�ߺ�üũ SQL ���� ���� �߻�!");
			e.printStackTrace();
		}
		
		return -1; // ���� �߻�
	}
	
	public int storeBlock(Block block, String privateKey) {
		String SQL = "INSERT INTO BLOCKTABLE VALUES( ?, ?,?, ?, ?, ?,?)";
		
		try {
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, block.getNum()+"");
			pstmt.setString(2, block.getNonce());
			pstmt.setString(3, block.getTimestamp());
			pstmt.setString(4, "�ŷ�����");
			pstmt.setString(5, block.getPreviousBlockHash());
			pstmt.setString(6, block.getHash());
			pstmt.setString(7, privateKey);
			
			int result = pstmt.executeUpdate();
			return result; // ������ ��ģ Ʃ�� ���� ��ȯ
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("DB ���� ���� ����!");
			e.printStackTrace();
		}
		return -1; // DB ���� �߻� ��
	}
	
	public int deleteAllBlock(String privateKey) {
		String SQL = "DELETE FROM BLOCKTABLE WHERE privatekey = ?";
		
		
		try {
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, privateKey);
			
			int result = pstmt.executeUpdate();
			System.out.println(result +"�� DB���� ����");
			
			return result;
		} catch (SQLException e) {
			System.out.println("���� ���� ���� �� SQL ���� ���� �߻�");
		}
		
		return -1; //DB ���� 
	}
	
	public LinkedList<Block> getBlocks(String privateKey) {
		String SQL = "SELECT * FROM BLOCKTABLE WHERE privatekey = ?";
		LinkedList<Block> blocks = new LinkedList<Block>(); // �߰� ������ ���� �Ͼ ���̹Ƿ� ��ũ�帮��Ʈ�� ���ش�.
		Block block=null;
		
		try {
			
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, privateKey);
			
			ResultSet rs = pstmt.executeQuery();
			// ����Ʈ �����ϱ� 
			while(rs.next()) {
				block = new Block(rs.getString("previoushash"),rs.getString("nonce"),rs.getString("timestamp"),Integer.valueOf(rs.getString("num")));
				if(blocks.size()==0) {
					blocks.add(block);
				}
				
				else {
					for(int i = 0; i< blocks.size(); i++) {
						if(block.getNum() < blocks.get(i).getNum()) {
							blocks.add(i,block);
							break;
						}
						if(i==(blocks.size()-1)) {
							blocks.add(block);
							break; // �� �극��ũ�� ���ϸ� ���� ������ ���°�?
						}
					}
				}
			}
			
			return blocks;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("DB���� ���� �������� �� ���� �߻�!");
			e.printStackTrace();
		}
		
		return null; // db ���� 
		
		
	}
	
	

}