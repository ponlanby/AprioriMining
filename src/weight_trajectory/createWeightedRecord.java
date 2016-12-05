package weight_trajectory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class createWeightedRecord {
	
	public static void main(String[] args){
//		System.out.println(normalize(3,10,1));
		updateWeightedRecord();
	}
	
	public static double calculateWeight(double w1, double w2, double w3){
		double a = 0.2;
		double b = 0.3;
		double c = 0.5;
		double weight = (double)(a*w1+b*w2+c*w3)/(double)(w1+w2+w3);
		return weight;
		
	}
	public static void updateWeightedRecord(){
		try{
			Connection conn = getConnection();
			
			Statement st = conn.createStatement();
			Statement st2 = conn.createStatement();
			ResultSet rs = null;			
			String getRecord = "select id, cat_level, browse_time, enquiry from `weighted_record`";
			
			rs = st.executeQuery(getRecord);
			while(rs.next()){
				String id = rs.getString(1);
				Double cat_level = rs.getDouble(2);
				Double browse_time = rs.getDouble(3);
				Double enquiry = rs.getDouble(4);
				double weight = calculateWeight(cat_level, browse_time, enquiry);
				DecimalFormat df = new DecimalFormat("0.000");
				String btw = df.format(weight);
				String updateRecord = "update `weighted_record` set weight=" + btw + " where id=" + id;
				st2.executeUpdate(updateRecord);
//				System.out.println(btw);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static double normalize(int x, int max_x, int min_x){
		double weight = (double)(x-min_x)/(double)(max_x-min_x);
		if(weight>1.0){
			weight = weight%1.0;
		}
		return weight;
	}
	
	public static void updateNormalizedRecord(){
		try{
			Connection conn = getConnection();
			
			Statement st = conn.createStatement();
			Statement st2 = conn.createStatement();
			ResultSet rs = null;			
			String getRecord = "select id, cat_level, browse_time from `record`";
			
			rs = st.executeQuery(getRecord);
			while(rs.next()){
				String id = rs.getString(1);
				String cat_level = rs.getString(2);
				String browse_time = rs.getString(3);
				double cat_level_weight = normalize(Integer.parseInt(cat_level), 3, 1);
				double browse_time_weight = normalize(Integer.parseInt(browse_time), 210, 0);
				DecimalFormat df = new DecimalFormat("0.000");
				String btw = df.format(browse_time_weight);
//				System.out.println(btw);
				String updateRecord = "update `weighted_record` set cat_level=" + cat_level_weight + ", browse_time=" + btw + " where id=" + id;
				st2.executeUpdate(updateRecord);
//				System.out.println(updateRecord);
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
