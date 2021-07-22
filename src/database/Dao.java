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
			Connection connection = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE WHERE username = ?";
			
			// ���ɻ� : SQL�� ���� ��ü ����
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1,userName);
			
			// ���ɻ� : userName �ߺ� üũ
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return 0; // �ߺ��� userName�� ����
			}else {
				return 1; // �ߺ��� �� ��.
			}
		} catch (SQLException e) {
			System.out.println("SQL�� ���� �� ���� �߻�");
			return -1; // SQL�� ���� �� ���� �߻�
		}	
	}
	
	
	
	

}
