package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import objects.InfoCNMV;
import objects.RegistryList;

/*
CREATE TABLE IF NOT EXISTS `financialinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url_ixbrl` text NOT NULL,
  `url_info_context` text NOT NULL,
  `entity_name` text NOT NULL,
  `entity_code` text NOT NULL,
  `period_end` text NOT NULL,
  `form` text NOT NULL,
  `format` text NOT NULL,
  `hash_code` varchar(64) NOT NULL,
  `oam` text NOT NULL,
  `country` text NOT NULL,
  PRIMARY KEY (`id`)
);
 */

public class DataBase {
	
	private Connection connection;
	
	public DataBase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/cnmv", "admin", "admin");
		} catch (SQLException e) {
			System.err.println("SQL exception...");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found. Check the MySQL access driver...");
			e.printStackTrace();
		}
	}
	
	public boolean hasRegistries() {
		
		String query = "SELECT COUNT(*) FROM financialinfo";
		
		try {
			boolean hasRegistries = false;
			int totalRegistries = 0;
			
			Statement sentence = connection.createStatement();
			ResultSet resul = sentence.executeQuery(query);
			
			while (resul.next()) {totalRegistries = resul.getInt(1);}
			if (totalRegistries > 0) {hasRegistries = true;}			
			
			resul.close();
			sentence.close();
			
			return hasRegistries;
			
		} catch (SQLException e) {
			System.err.println("SQL exception...");
			e.printStackTrace();
			System.exit(1);
		}
		
		return false;
	}

	public void store(RegistryList list) {
		
		int rowsInserted = 0;
		
		System.out.println("\tIns\tMessage");
		
		for (int i = list.size() - 1; i <= list.size() && i >= 0; i--) {
			
			InfoCNMV registry = list.get(i);

			String query = String.format("INSERT INTO financialinfo (url_ixbrl, url_info_context, entity_name, entity_code, period_end, form, format, hash_code, oam, country) SELECT '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s' FROM dual WHERE NOT EXISTS (SELECT url_info_context FROM financialinfo WHERE url_info_context = '%s') LIMIT 1",
					registry.getUrl_ixbrl(),
					registry.getUrl_info_context(),
					registry.getEntity_name(),
					registry.getEntity_code(),
					registry.getPeriod_end(),
					registry.getForm(),
					registry.getFormat(),
					registry.getHash_code(),
					registry.getOam(),
					registry.getCountry(),
					registry.getUrl_info_context()
					);

			int newRows = 0;

			try {
				Statement sentence = connection.createStatement();
				newRows = sentence.executeUpdate(query);
				rowsInserted += newRows;
				if (newRows > 0) {
					System.out.println("\t" + rowsInserted + "\tRegistry inserted!");
				}
			} catch (SQLException e) {
				System.err.println("SQL exception in the insertion.");
				System.err.printf("Message  : %s %n", e.getMessage());
				System.err.printf("SQL state: %s %n", e.getSQLState());
				System.err.printf("Cod error: %s %n", e.getErrorCode());
			}
		}
		
		System.out.println("\n\tTOTAL: " + rowsInserted + " insertions.");
		
	}

	public String getLastUrlInfoContext() {
		
		String query = "SELECT url_info_context FROM financialinfo ORDER by ID DESC LIMIT 1";
		String lastContext = "NULL";
		
		try {			
			Statement sentence = connection.createStatement();
			ResultSet resul = sentence.executeQuery(query);
			
			while (resul.next()) {
				lastContext = resul.getString("url_info_context");
			}
			
			resul.close();
			sentence.close();
			
			return lastContext;
			
		} catch (SQLException e) {
			System.err.println("SQL exception...");
			e.printStackTrace();
			System.exit(1);
		}
		
		return lastContext;
	}
	
}