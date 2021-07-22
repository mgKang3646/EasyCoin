package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dao {
	
	// 관심사 : UserName 중복 체크
	public int checkDuplicateUserName(String userName) {
		try {
			// 관심사 : 커넥션 객체 생성
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection connection = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE WHERE username = ?";
			
			// 관심사 : SQL문 실행 객체 생성
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1,userName);
			
			// 관심사 : userName 중복 체크
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return 0; // 중복된 userName이 존재
			}else {
				return 1; // 중복이 안 됨.
			}
		} catch (SQLException e) {
			System.out.println("SQL문 실행 중 오류 발생");
			return -1; // SQL문 실행 중 문제 발생
		}	
	}
	
	
	
	

}
