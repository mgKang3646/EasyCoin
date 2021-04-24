package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
	
	public static Connection getConnection() {
		
		try {
			
			String dbURL = "jdbc:mysql://localhost:3306/BLOCKCHAIN";
			String dbID = "root";
			String dbPassword = "chmo5847*";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 연결 성공!");
			
			return DriverManager.getConnection(dbURL, dbID, dbPassword); //Connection 객체를 반환
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("드라이버 연결 실패 ㅠㅠ");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("db접근 실패 ㅠㅠ");
			e.printStackTrace();
			
		}
		return null;
	}

}
