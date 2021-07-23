package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	// ���ɻ� ȸ������ ���� DB ����
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
	
	
	
	

}
