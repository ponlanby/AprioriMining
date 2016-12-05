
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAO {
	
	/*public static void main(String[] args){
		ArrayList<String> str = new ArrayList<String>();
		DAO db = new DAO();
		str = db.getProductName();
	}*/
	
	public ArrayList<String> getSearchKeyword(){
		String sql = null;
		ArrayList<String> result = new ArrayList<String>();
		try{
			Connection conn = getConnection();
			
			sql = "Select SEARCH_WORD_NEW from service";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while( rs.next() )
			{
				result.add( rs.getString( "SEARCH_WORD_NEW" ) );
			}
			
			conn.close();
		}
		catch(SQLException e){
			System.out.println(sql);
		}
		
		return result;
	}

	public ArrayList<String> getProductKeyword(){
		
		String sql = null;
		ArrayList<String> result = new ArrayList<String>();
		try{
			Connection conn = getConnection();
			
			sql = "Select PROD_KEYWORD from service";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while( rs.next() )
			{
				result.add( rs.getString( "PROD_KEYWORD" ) );
			}
			
			conn.close();
		}
		catch(SQLException e){
			System.out.println(sql);
		}
		
		return result;
	}
	
	public ArrayList<String> getProductName(){
		String sql = null;
		ArrayList<String> result = new ArrayList<String>();
		try{
			Connection conn = getConnection();
			
			sql = "Select PROD_NAME from service";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while( rs.next() )
			{
				result.add( rs.getString( "PROD_NAME" ) );
			}
			
			conn.close();
		}
		catch(SQLException e){
			System.out.println(sql);
		}
		
		return result;
	}
	
	public Connection getConnection()
	{
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
				
			con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/b2b_db", "root", "trc20183");
			} catch ( SQLException e ){
				System.out.println( "Failed to connect DB!" );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return con;
	}
}
