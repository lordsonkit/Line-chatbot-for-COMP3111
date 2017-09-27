package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		try {
		Connection connection=getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT keyword, response FROM phrase where keyword like concat('%', ?, '%')");
		stmt.setString(1, text);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
//			System.out.println("ID: " + rs.getInt(1) + "\tName: " + rs.getString(2) + "\tPhone: "+ rs.getString(3));
			result=rs.getString(2);
		} else {
			throw new Exception("NOT FOUND");
		}
		rs.close();
		stmt.close();
		connection.close();
		return result;
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//if (result != null)
		//	return result;
		//throw new Exception("NOT FOUND");
		
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
