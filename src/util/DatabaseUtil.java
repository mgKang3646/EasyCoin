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
			System.out.println("����̹� ���� ����!");
			
			return DriverManager.getConnection(dbURL, dbID, dbPassword); //Connection ��ü�� ��ȯ
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("����̹� ���� ���� �Ф�");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("db���� ���� �Ф�");
			e.printStackTrace();
			
		}
		return null;
	}

}
