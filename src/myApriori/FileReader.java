package myApriori;
 
import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.InputStreamReader; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList ;
import java.util.List ;

public class FileReader 
{
//        public static List<List<String>> getDatabase()
//        {
//                List<List<String>> db = new ArrayList<List<String>>() ;
//                
//                try
//                {
//                        File file = new File("data.txt") ;
//                        
//                        if ( file.isFile() && file.exists())
//                        {
//                                InputStreamReader read = new InputStreamReader
//                                                        (
//                                                        new FileInputStream(file)
//                                                                        ) ;
//                                
//                                BufferedReader reader = new BufferedReader( read ) ;
//                                
//                                
//                                String line = null ;
//                                
//                                while ( (line = reader.readLine())!= null )
//                                {
//                                        String [] strToknizer = line.split(" ") ;
//                                        
//                                        List<String> tmpLine = new ArrayList<String>() ;
//                                        
//                                        for ( int i = 1 ; i < strToknizer.length ; i++ )
//                                        {
//                                                
//                                                tmpLine.add(strToknizer[i]) ;
//                                                
//                                        }
//                                        db.add(tmpLine) ;
//                                }
//                                
//                                reader.close();
//                        }
//                        else
//                        {
//                                System.out.println("fail to find target file !");
//                        }
//                }
//                catch (Exception e)
//                {
//                        System.out.println("fail in reading file\'s content ");
//                        e.printStackTrace();
//                }
//                
//                return db ;
//        }
        
        public static List<List<String>> getDatabase(){
        	List<List<String>> db = new ArrayList<List<String>>() ;
        	try{
        		Connection conn = getConnection();
    			
    			Statement st = conn.createStatement();
    			Statement st2 = conn.createStatement();
    			ResultSet rs = null;
    			String sql = "select product_id,search_word1,search_word2,search_word3,search_word4,search_word5,search_word6,search_word7,search_word8,search_word9,search_word10,weight from `weighted_trajectory`";
    			
    			rs=st.executeQuery(sql);
    			while(rs.next()){
    				String product_id = rs.getString("product_id");
    				String weight = rs.getString("weight");
    				String search_word1 = rs.getString("search_word1");
    				String search_word2 = rs.getString("search_word2");
    				String search_word3 = rs.getString("search_word3");
    				String search_word4 = rs.getString("search_word4");
    				String search_word5 = rs.getString("search_word5");
    				String search_word6 = rs.getString("search_word6");
    				String search_word7 = rs.getString("search_word7");
    				String search_word8 = rs.getString("search_word8");
    				String search_word9 = rs.getString("search_word9");
    				String search_word10 = rs.getString("search_word10");
    				
    				List<String> temp = new ArrayList<String>();
    				temp.add(search_word1);
    				temp.add(search_word2);
//    				temp.add(search_word3);
//    				temp.add(search_word4);
//    				temp.add(search_word5);
//    				temp.add(search_word6);
//    				temp.add(search_word7);
//    				temp.add(search_word8);
//    				temp.add(search_word9);
//    				temp.add(search_word10);
    				db.add(temp);
    			}
    			System.out.println("load db finished");
    			conn.close();
        	}
        	catch(Exception e){
        		e.printStackTrace();
        	}
        	return db;
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
