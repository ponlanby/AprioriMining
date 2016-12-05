package weight_trajectory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class CreateInitLog {
	
	public static void main(String[] args){
		setEnquiry();
//		setBrowseTime();
	}
	
	public static void setEnquiry(){
		try{
			int count=0;
			Connection conn = getConnection();
//			String getEnquiryField = "SELECT SEND_TIME,SENDER_IP_TONUMBER,UNIC_COOKIE_ID from `enquiry`";

			String getEnquiryField = "SELECT * from `record` INNER JOIN `enquiry` ON record.IPTONUMBER=enquiry.SENDER_IP_TONUMBER AND record.LAST_VISIT_TIME<=enquiry.SEND_TIME AND record.VISIT_TIME>=enquiry.SEND_TIME";
			//String[] enquiry = new String[3];
			String visit_id = null;
			
			Statement st = conn.createStatement();
			ResultSet rs = null;
			Statement st2 = conn.createStatement();
			try{
				rs = st.executeQuery(getEnquiryField);
				while(rs.next()){
					visit_id=rs.getString(1);
					System.out.println(visit_id);
					String updateSql = "update `record` set ENQUIRY=1 where ID=" + visit_id;
					System.out.println(updateSql);
					st2.executeUpdate(updateSql);
					count++;
//					System.out.println(updateSql);
				}
//				System.out.println(count);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void setBrowseTime(){
		try{
			Connection conn = getConnection();
			String getVisitTime = "SELECT ID,TIME_TO_SEC(TIMEDIFF(VISIT_TIME,LAST_VISIT_TIME)) FROM `record`";
			String[] visitTime = new String[2];
			
			Statement st = conn.createStatement();
			ResultSet rs = null;
			Statement st2 = conn.createStatement();
			try{
				rs = st.executeQuery(getVisitTime);
				while(rs.next()){
					visitTime[0] = rs.getString(1);
					visitTime[1] = rs.getString(2);
					if(visitTime[1]==null){
						visitTime[1]="0";
					}
					String updateSql = "update `record` set BROWSE_TIME=" + visitTime[1] + " where ID=" + visitTime[0];
					st2.executeUpdate(updateSql);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testLoad(){
		try{
			Connection conn = getConnection();
			
			String[] recordItem = new String[9];
			String[] categoryItem = new String[4];
			String[] enquiryItem = new String[3];
			
			ArrayList<String[]> record = new ArrayList();
			ArrayList<String[]> category = new ArrayList();
			ArrayList<String[]> enquiry = new ArrayList();
			
			String getRecordSql = "select * from `record` where 'search_word_new'<>-1";
			String getCategorySql = "select * from `category`";
			String getEnquirySql = "select * from `enquiry`"; 

			Statement st = conn.createStatement();
			ResultSet rs = null;
			
			try{
//				rs = st.executeQuery(getRecordSql);
//				while(rs.next()){
//					recordItem[0] = rs.getString("PRODUCT_ID");
//					recordItem[1] = rs.getString("IPTONUMBER");
//					recordItem[2] = rs.getString("VISIT_TIME");
//					recordItem[3] = rs.getString("LAST_VISIT_TIME");
//					recordItem[4] = rs.getString("COOKIE");
//					recordItem[5] = rs.getString("SEARCH_WORD_NEW");
//					recordItem[6] = rs.getString("CATEGORY_NUMBER");
//					recordItem[7] = rs.getString("CAT_LEVEL");
//					recordItem[8] = rs.getString("PROD_KEYWORD");
//					record.add(recordItem);
//				}
//				System.out.println("record success");
				
				rs = st.executeQuery(getCategorySql);
				while(rs.next()){					
					categoryItem[0] = rs.getString("CATEGORY_CODE");
					categoryItem[1] = rs.getString("CATEGORY_LEVEL1_CODE");
					categoryItem[2] = rs.getString("CATEGORY_LEVEL2_CODE");
					categoryItem[3] = rs.getString("CATEGORY_LEVEL");
					category.add(categoryItem);
				}
				System.out.println("category success");
//				
//				rs = st.executeQuery(getEnquirySql);
//				while(rs.next()){					
//					enquiryItem[0] = rs.getString("SEND_TIME");
//					enquiryItem[1] = rs.getString("SENDER_IP_TONUMBER");
//					enquiryItem[2] = rs.getString("UNIC_COOKIE_ID");
//					enquiry.add(enquiryItem);
//				}
//				System.out.println("enquiry success");
				
				
//				Iterator record_it = record.iterator();
//				Iterator category_it = category.iterator();
//				Iterator enquiry_it = enquiry.iterator();
				
				for(int i=0; i<category.size(); i++){
					String[] str = new String[category.size()];
					
					str = category.get(i);
					System.out.println(str[0]);
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createInitLog(){
		String sql = null;
		
		try{
			Connection conn = getConnection();
			
			ArrayList<String> getRecord = new ArrayList();
			String getRecordSql = "select * from `record` where 'search_word_new'<>-1";
//			String getCategorySql = "select * from 'category'";
			String getEnquirySql = "select * from 'enquiry'";
			
//			String createInitLogSql = "create table 'initlog'("
//					+ "visited_id varchar(255) not null auto_increment,"
//					+ "search_word, varchar(255),"
//					+ "product_id, varchar(255),"
//					+ "product_keyword, varchar(255),"
//					+ "visit_time, varchar(255),"
//					+ "browse_time, varchar(255),"
//					+ "enquiry, varchar(255)"
//					+ ")";
//			
//			String visited_id 			= null;
//			String search_word 			= null;
//			String product_id			= null;
//			String product_keyword 		= null;
//			String visit_time			= null;
//			String browse_time			= null;
//			String enquiry				= null;
			
			
			
			
			
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while( rs.next() )
			{
				//result.add( rs.getString( "SEARCH_WORD_NEW" ) );
			}
			
			conn.close();
		}
		catch(SQLException e){
			System.out.println(sql);
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
