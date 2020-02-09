package db;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class HistoricalPricesDaily {
	static String table = "historical_prices_daily";
	static String dbUrl = "jdbc:mysql://localhost:3306/testDB";
	static String dbUser = "root";
	static String dbpwd = "root";
	
	public static void main(String args[]) {
		Date date = new Date(System.currentTimeMillis());
		insert("ITC", 1,2,3,4,5,6,7,8,1000000,date);
		//select();
	}
	public static void select() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(dbUrl, dbUser, dbpwd);
			Statement stmt = connect.createStatement();
			
			String query = "select * from " + table;
			
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  "
						+ rs.getString(4) + "  " + rs.getString(5) + "  " + rs.getString(6) + "  " + rs.getString(7)
						+ "  " + rs.getString(8) + "  " + rs.getString(9) + "  " + rs.getString(10) + "  "
						+ rs.getString(11) + "  " + rs.getString(12) + "  " + rs.getString(13)+ "  " + rs.getString(14) );
			connect.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * 
	String symbol,
	float open_price ,
    float close_price ,
    float prev_close_price ,
    float high_price,
    float low_price,
    float ltp_price,
    float high52,
    float low52, 
    BIGINT vol ,
    DATE mTIMESTAMP ,
	 */
	public static void insert(String symbol, float open_price, float close_price, float prev_close_price,
			float high_price, float low_price, float ltp_price, float high52, float low52, int vol,
			Date mTIMESTAMP) {
		try {
			Timestamp created_on = new java.sql.Timestamp(new java.util.Date().getTime());
			
			
			Class.forName("com.mysql.jdbc.Driver");
			
			
			
			
			Connection connect = DriverManager.getConnection(dbUrl, dbUser, dbpwd);

			// PreparedStatements can use variables and are more efficient
			PreparedStatement preparedStatement = connect.prepareStatement(
					"insert into  historical_prices_daily values (default, ?, ?,?, ?,?, ?,?, ?,?, ?,?, ?,?)");
			// Parameters start with 1
			preparedStatement.setString(1, symbol);
			preparedStatement.setFloat(2, open_price);
			preparedStatement.setFloat(3, close_price);
			preparedStatement.setFloat(4, prev_close_price);
			preparedStatement.setFloat(5, high_price);
			preparedStatement.setFloat(6, low_price);
			preparedStatement.setFloat(7, ltp_price);
			preparedStatement.setFloat(8, high52);
			preparedStatement.setFloat(9, low52);
			preparedStatement.setInt(10, vol);
			preparedStatement.setDate(11, mTIMESTAMP);
			preparedStatement.setTimestamp(12, created_on);
			preparedStatement.setTimestamp(13, created_on);
			preparedStatement.executeUpdate();
			connect.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
