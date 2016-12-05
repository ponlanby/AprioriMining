package weight_trajectory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class CreateTrajectory {
	
	public static void main(String[] args){
		createTrajectory();
	}
	
	public static void createTrajectory(){
		try{
			Connection conn = getConnection();
			
			Statement st = conn.createStatement();
			Statement st2 = conn.createStatement();
			Statement st3 = conn.createStatement();
			ResultSet rs = null;			
			String getRecord = "select ID, PRODUCT_ID, GROUP_CONCAT(SEARCH_WORD_NEW), WEIGHT, CATEGORY_NUMBER from `temp_trajectory` GROUP BY PRODUCT_ID";
			
			rs = st.executeQuery(getRecord);
			int max_count = 0;
			while(rs.next()){
				String id = rs.getString("ID");
				String product_id = rs.getString("PRODUCT_ID");
//				String product_kw = rs.getString(3);
				String search_word = rs.getString("GROUP_CONCAT(SEARCH_WORD_NEW)");
				String weight = rs.getString("WEIGHT");
				String category = rs.getString("CATEGORY_NUMBER");
				
//				if(id.equals("771983")){
//					System.out.println(search_word);
//				}
				
				String sql1 = "insert into trajectory(ID, PRODUCT_ID, WEIGHT, CATEGORY_NUMBER) values('" + id + "', '" + product_id + "', '" + weight + "', '" + category + "')";
				st2.execute(sql1);
				
				HashSet<String> kw = new HashSet<String>();
				search_word = search_word.replace("%20", " ");
				search_word = search_word.replace("%21", "!");
				search_word = search_word.replace("%22", "\"");
				search_word = search_word.replace("%23", "#");
				search_word = search_word.replace("%24", "$");
				search_word = search_word.replace("%25", "\\%");
				search_word = search_word.replace("%26", ",");
				search_word = search_word.replace("%27", "'");
				search_word = search_word.replace("%28", "(");
				search_word = search_word.replace("%29", ")");
				search_word = search_word.replace("%2A", "*");
				search_word = search_word.replace("%2B", ",");			
				search_word = search_word.replace("%2C", ",");		
				search_word = search_word.replace("%2F", ",");
				search_word = search_word.replace("%3A", ":");	
				search_word = search_word.replace("%3B", ";");
				search_word = search_word.replace("%3D", "=");
				search_word = search_word.replace("%40", "@");
				search_word = search_word.replace("'", "");
				
				String[] kwlist1 = search_word.split(",|\\+");
				for(int i=0; i<kwlist1.length; i++){
					if(!kwlist1[i].equals("")){
						String word = kwlist1[i].toLowerCase();
						word = word.trim();
						kw.add(word);
					}
				}
				Iterator it = kw.iterator();
				int count = 1;
				while(it.hasNext()){
					String sql2 = "update `trajectory` set search_word" + count + "='" + (String)it.next() + "' where id=" + id;	
					count++;
					if(count>15){
						System.out.println(id);
						continue;
					}
//					System.out.println(sql2);
					if(!id.equals("2855618")
							&&!id.equals("126221")
							&&!id.equals("3588405")
							&&!id.equals("3591640")
							&&!id.equals("1933393")
							&&!id.equals("4864308")
							&&!id.equals("771983")){
						st3.executeUpdate(sql2);						
					}
				}
			}
			
			conn.close();
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
