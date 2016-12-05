package ne;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class fup {
	private final static int SUPPORT = 2; // 支持度阈值
	private final static double CONFIDENCE = 0.7;
	// 置信度阈值
	private final static String ITEM_SPLIT = ";";
	// 项之间的分隔符
	private final static String CON = "->";
	
	private static ArrayList<String> getDataFromDB(int start, int end) throws Exception{
		ArrayList<String> dataList = new ArrayList<>();
		
		String driver = "com.mysql.jdbc.Driver"; // 连接数据库
		Class.forName(driver);
		Connection connecter = DriverManager.getConnection( "jdbc:mysql://localhost:3306/b2b_db?useSSL=false", "root", "trc20183");
		if (!connecter.isClosed())
			System.out.println("success in getConnetion");
		Statement statement = connecter.createStatement();
		String sql = "select search_word1,search_word2,search_word3,weight from `weighted_trajectory` limit " + start + ", " + end;
		ResultSet rs = statement.executeQuery(sql);

		String pw = null;
		String pw2 = null;
		while (rs.next()) {
			String sw1 = rs.getString("search_word1");
			String sw2 = rs.getString("search_word2");
			String sw3 = rs.getString("search_word3");
			String weight = rs.getString("weight");
			if(!sw1.equals("null")){
				pw2 = sw1;
			}
			if(!sw2.equals("null")){
				pw2 += ITEM_SPLIT + sw2;
			}
			if(!sw3.equals("null")){
				pw2 += ITEM_SPLIT + sw3;
			}
			dataList.add(pw2);
		}
		return dataList;
	}

	public static void main(String[] args) throws Exception{
//		readFromTxt();
		long starTime=System.currentTimeMillis();
		
		ArrayList<String> dataList_D = getDataFromDB(0, 600);
		ArrayList<String> dataList_d = getDataFromDB(601, 400);
		Map<String, Integer> candidateSetMap_D = getDataFromTXT("frequentSet"); 
		Map<String, Integer> candidateSetMap_d = getDataFromTXT("candidateSet"); 
		Set<String> C_D = candidateSetMap_D.keySet();
		Set<String> C_d = candidateSetMap_d.keySet();
		
		
		improved_apriori apriori2 = new improved_apriori();
		Map<String, Integer> frequentSetMap_D = apriori2.apriori(dataList_D);
		Map<String, Integer> frequentSetMap_d = apriori2.apriori(dataList_d);
//		
		Set<String> L_D = frequentSetMap_D.keySet();
		Set<String> L_d = frequentSetMap_d.keySet();
//		//result set
		Map<String, Integer> resultSetMap = new HashMap<>();
		
		ArrayList<String> temp_D = new ArrayList<>();
		ArrayList<String> temp_d = new ArrayList<>();
		//case1: itemset is frequent in D&d
		for(String D:L_D){
			int sup = frequentSetMap_D.get(D);
			if(sup >= SUPPORT){
				if(resultSetMap.containsKey(D)){
					sup += resultSetMap.get(D);
				}
				resultSetMap.put(D, sup);
				System.out.println(D + " " + sup);
				temp_D.add(D);
			}
		}
		//L_D.remove(D) && C_D.remove(D);
		for(String t:temp_D){
			Iterator it1 = L_D.iterator();
			while(it1.hasNext()){
				String str = (String) it1.next();
				if(str.equals(t)){
					it1.remove();
				}
			}
			Iterator it2 = C_D.iterator();
			while(it2.hasNext()){
				String str = (String) it2.next();
				if(str.equals(t)){
					it2.remove();
				}
			}
		}
		temp_D.clear();
		for(String d:L_d){
			int sup = frequentSetMap_d.get(d);
			if(sup >= SUPPORT){
				if(resultSetMap.containsKey(d)){
					sup += resultSetMap.get(d);
				}
				resultSetMap.put(d, sup);
				System.out.println(d + " " + sup);
				temp_d.add(d);
			}
		}
		//L_d.remove(d) && C_d.remove(d);
		for(String t:temp_d){
			Iterator it1 = L_d.iterator();
			while(it1.hasNext()){
				String str = (String) it1.next();
				if(str.equals(t)){
					it1.remove();
				}
			}
			Iterator it2 = C_d.iterator();
			while(it2.hasNext()){
				String str = (String) it2.next();
				if(str.equals(t)){
					it2.remove();
				}
			}
		}
		temp_d.clear();
		for(String d:L_d){
			for(String D:L_D){
				if(d.equals(D)){
					//计算支持度
					int sup = frequentSetMap_D.get(D) + frequentSetMap_d.get(d);
					//add to resultset
					resultSetMap.put(D, sup);
					System.out.println(D + " " + sup);
					temp_D.add(D);
					temp_d.add(d);
					continue;
				}
			}
		}
		//L_D.remove(D) && C_D.remove(D)
		for(String t:temp_D){
			Iterator it1 = L_D.iterator();
			while(it1.hasNext()){
				String str = (String) it1.next();
				if(str.equals(t)){
					it1.remove();
				}
			}
			Iterator it2 = C_D.iterator();
			while(it2.hasNext()){
				String str = (String) it2.next();
				if(str.equals(t)){
					it2.remove();
				}
			}
		}
		temp_D.clear();
		//L_d.remove(d) && C_d.remove(d);
		for(String t:temp_d){
			Iterator it1 = L_d.iterator();
			while(it1.hasNext()){
				String str = (String) it1.next();
				if(str.equals(t)){
					it1.remove();
				}
			}
			Iterator it2 = C_d.iterator();
			while(it2.hasNext()){
				String str = (String) it2.next();
				if(str.equals(t)){
					it2.remove();
				}
			}
		}
		temp_d.clear();
		
		//case2: itemset is frequent only in D
		for(String D:L_D){
			for(String d:C_d){
				if(D.equals(d)){
					//calculate support
					int sup = frequentSetMap_D.get(D) + candidateSetMap_d.get(d);
					if(sup >= SUPPORT){
						resultSetMap.put(D, sup);
//						System.out.println(D + sup);
						temp_D.add(D);
						temp_d.add(d);
					}
					continue;
				}
			}
		}
		//L_D.remove(D) && C_D.remove(D)
		for(String t:temp_D){
			Iterator it1 = L_D.iterator();
			while(it1.hasNext()){
				String str = (String) it1.next();
				if(str.equals(t)){
					it1.remove();
				}
			}
			Iterator it2 = C_D.iterator();
			while(it2.hasNext()){
				String str = (String) it2.next();
				if(str.equals(t)){
					it2.remove();
				}
			}
		}
		temp_D.clear();
		//C_d.remove(d)
		for(String t:temp_d){
			Iterator it = C_d.iterator();
			while(it.hasNext()){
				String str = (String) it.next();
				if(str.equals(t)){
					it.remove();
				}
			}
		}
		temp_d.clear();
		
		//case3: itemset is frequent only in d
		for(String d:L_d){
			for(String D:C_D){
				if(d.equals(D)){
					int sup = frequentSetMap_d.get(d) + candidateSetMap_D.get(D);
					if(sup >= SUPPORT){
						resultSetMap.put(d, sup);
//						System.out.println(d + sup);
						L_d.remove(d);
						C_D.remove(D);
						C_d.remove(d);
					}
					continue;
				}
			}
		}
		//C_D.remove(D)
		for(String t:temp_D){
			Iterator it = C_D.iterator();
			while(it.hasNext()){
				String str = (String) it.next();
				if(str.equals(t)){
					it.remove();
				}
			}
		}
		temp_D.clear();
		//L_d.remove(d) && C_d.remove(d)
		for(String t:temp_d){
			Iterator it1 = L_d.iterator();
			while(it1.hasNext()){
				String str = (String) it1.next();
				if(str.equals(t)){
					it1.remove();
				}
			}
			Iterator it2 = C_d.iterator();
			while(it2.hasNext()){
				String str = (String) it2.next();
				if(str.equals(t)){
					it2.remove();
				}
			}
		}
		temp_d.clear();
		
		//case4: itemset is not frequent in D|d
		
		long endTime=System.currentTimeMillis();
		long Time=endTime-starTime;
		System.out.println("Time:" + Time);
		
		writeResultToTxt(resultSetMap);
	}
	
	public static void writeResultToTxt(Map<String, Integer> resultSetMap){
		Set<String> keySet = resultSetMap.keySet();
		BufferedWriter fw = null;
		try {
			File file = new File("D://resultSet.txt");
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
			int count=0;
			for (String key : keySet) {
				String[] keys =key.split(ITEM_SPLIT);
				if(keys.length==1){
					count++;
					fw.append(key + "				支持度：" + resultSetMap.get(key));
					fw.newLine();
				}
			}		
			count=0;
			for (String key : keySet) {
				String[] keys =key.split(ITEM_SPLIT);
				if(keys.length==2){
					count++;
					fw.append(key + "				支持度：" + resultSetMap.get(key));
					fw.newLine();
				}
			}	
			count=0;
			for (String key : keySet) {
				String[] keys =key.split(ITEM_SPLIT);
				if(keys.length==3){
					count++;
					fw.append(key + "				支持度：" + resultSetMap.get(key));
					fw.newLine();
				}
			}			
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Map<String, Integer> getDataFromTXT(String fileName){
		Map<String, Integer> res = new TreeMap<>();
		String filePath = "D://" + fileName + ".txt";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
			String str = null;
			while ((str = reader.readLine()) != null) {
				String[] strs = str.split("				支持度：");
				if(strs.length==2){
					String str1 = strs[0];
					String str2  =strs[1];
//					System.out.println(str1);
//					System.out.println(str2);
					res.put(str1, Integer.parseInt(str2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println(fileName);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return res;
	}
}
