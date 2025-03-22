package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Electricity_biling {
	
	static void createDB(String dbname) {
		try {
		Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/","postgres", "123");
		String query="Create database "+dbname;//Create Query
		Statement stmt=con.createStatement();//Create Statement
		stmt.executeLargeUpdate(query);
		}catch(SQLException e) {e.printStackTrace();}
		//Create Query
		}
	
	
	static void createTbl(String dbname,String Tblname) 
	{
	try {
		Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,"postgres", "123");
		String query = "CREATE TABLE IF NOT EXISTS " + Tblname + " (" +
				"customer_id SERIAL PRIMARY KEY, " +
			    "name VARCHAR(100), " +
			    "address VARCHAR(255)," +
			    "meter_number VARCHAR(50) UNIQUE)";
		Statement stmt=con.createStatement();//Create Statement
		stmt.executeLargeUpdate(query);
	}
	catch(SQLException e) {e.printStackTrace();}
	//Create Query
	}
	
	static void createTb2(String dbname,String Tb2name) 
	{
	try {
		Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,"postgres", "123");
		String query = "CREATE TABLE IF NOT EXISTS " + Tb2name + " (" +
				"billing_id SERIAL PRIMARY KEY, "  +
			    "units_consumed INT, " +
			    "amount DOUBLE PRECISION, " +
			    "billing_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
			    "customer_id int REFERENCES customer(customer_id)";
		Statement stmt=con.createStatement();//Create Statement
		stmt.executeLargeUpdate(query);
	}
	catch(SQLException e) {e.printStackTrace();}
	//Create Query
	}

	public static void main(String[] args) {
		
		//createDB("Electricdb");
		//createTbl("electricdb","customer");
		createTb2("electricdb","billing");
	}
	
}
