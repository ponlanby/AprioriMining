package weight_trajectory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddCategory {
	
	public static void main(String[] args){
		addCategory();
	}
	
	public static void addCategory(){
		try{
			Connection conn = getConnection();
			Statement st = conn.createStatement();
			Statement st2 = conn.createStatement();
			Statement st3 = conn.createStatement();
			ResultSet rs = null;
			ResultSet rs2 = null;
			String getRecord = "select product_id, category_number, category from `weighted_trajectory`";
			rs=st.executeQuery(getRecord);
			System.out.println("load finished");
			while(rs.next()){
				String id = rs.getString("product_id");
//				String category = rs.getString("category");
				if(id.equals("-1")){
					continue;
				}
				String category_number = rs.getString("category_number");
				String getCat = "select category from `weighted_trajectory` where product_id=" + id;
				rs2=st2.executeQuery(getCat);
				String category = null;
				if(rs2.next()){
					 category = rs2.getString("category");
				}
//				if(category!=null){
////					System.out.println(category);
//					continue;
//				}
				if(category.equalsIgnoreCase("null")){
					String updateCategory = "UPDATE `weighted_trajectory` SET category=(select CATEGORY_NAME_EN from `category` where CATEGORY_CODE=(select CATEGORY_LEVEL1_CODE from `category` where CATEGORY_NUMBER=" + category_number + ")) where CATEGORY_NUMBER="+category_number;
					System.out.println(updateCategory);
					st3.executeUpdate(updateCategory);
				}
				else{
					System.out.println("not null");
				}
//				else{					
//					String updateCategory = "UPDATE `weighted_trajectory` SET category=(select CATEGORY_NAME_EN from `category` where CATEGORY_CODE=(select CATEGORY_LEVEL1_CODE from `category` where CATEGORY_NUMBER=" + category_number + ")) where CATEGORY_NUMBER="+category_number;
//					System.out.println(updateCategory);
//					st3.executeUpdate(updateCategory);
//				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
				
			con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/b2b_db?useSSL=false", "root", "trc20183");
			} catch ( SQLException e ){
				System.out.println( "Failed to connect DB!" );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return con;
	}
}
