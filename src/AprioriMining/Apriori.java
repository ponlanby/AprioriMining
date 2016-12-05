package AprioriMining;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Apriori {
	
	private static ArrayList<String[]> data;
	
	private static int min_sup=0;
	private static int min_conf=0;
	
	public static void main(String[] args){
		apriori();
	}
	
	private static void apriori(){
		//连接计算的终止数，k项集必须算到k-1子项集为止
		int endNum_k = 3;
		//当前已经进行连接运算到几项集,开始时就是1项集
		int currentNum = 1;
		
		//生成频繁1项集
		Map<String, Integer> f1Set = createFrequent1Itemset(data);
		
		//频繁1项集信息得加入支持度
		Map<Set<String>, Integer> f1Map = new HashMap<Set<String>, Integer>();
		for(Map.Entry<String, Integer> f1Item : f1Set.entrySet()){
			Set<String> fs = new HashSet<String>();
			fs.add(f1Item.getKey());
			f1Map.put(fs, f1Item.getValue());
		}
		
		Map<Set<String>, Integer> result = f1Map;
		while(currentNum < endNum_k){
			result = genNextKItem(result);
			System.out.println("频繁" + currentNum + "项集：");
			for(Map.Entry<Set<String>, Integer> f1MapItem: result.entrySet()){
				System.out.println(f1MapItem.getValue());
			}
		}
		
		
	}
	
	private static Map<Set<String>, Integer> genNextKItem(Map<Set<String>, Integer> preMap) {
		// TODO Auto-generated method stub
		Map<Set<String>, Integer> result = new HashMap<Set<String>, Integer>();
		//遍历两个k-1项集生成k项集
		List<Set<String>> preSetArray = new ArrayList<Set<String>>();
		for(Map.Entry<Set<String>, Integer> preMapItem : preMap.entrySet()){
			preSetArray.add(preMapItem.getKey());
		}
		int preSetLength = preSetArray.size();
		for (int i = 0; i < preSetLength - 1; i++) {
			for (int j = i + 1; j < preSetLength; j++) {
				String[] strA1 = preSetArray.get(i).toArray(new String[0]);
				String[] strA2 = preSetArray.get(j).toArray(new String[0]);
				if (isCanLink(strA1, strA2)) { // 判断两个k-1项集是否符合连接成k项集的条件　
					Set<String> set = new TreeSet<String>();
					for (String str : strA1) {
						set.add(str);
					}
					set.add((String) strA2[strA2.length - 1]); // 连接成k项集
					// 判断k项集是否需要剪切掉，如果不需要被cut掉，则加入到k项集列表中
					if (!isNeedCut(preMap, set)) {//由于单调性，必须保证k项集的所有k-1项子集都在preMap中出现，否则就该剪切该k项集
						result.put(set, 0);
					}
				}
			}
		}
//		return assertFP(result);//遍历事物数据库，求支持度，确保为频繁项集
		return result;
	}
	
	/**获取k项集set的所有k-1项子集
	 * @param set 频繁k项集
	 * @return List<Set<String>> 所有k-1项子集容器
	 * @throws IOException 
	 */
	private List<Set<String>> getSubSets(Set<String> set) {
		// TODO Auto-generated method stub
		String[] setArray = set.toArray(new String[0]);
		List<Set<String>> result = new ArrayList<Set<String>>();
		for(int i = 0; i < setArray.length; i++){
			Set<String> subSet = new HashSet<String>();
			for(int j = 0; j < setArray.length; j++){
				if(j != i) subSet.add(setArray[j]);
			}
			result.add(subSet);
		}
		return result;
	}
	
	/**遍历事物数据库，求支持度，确保为频繁项集
	 * @param allKItem 候选频繁k项集
	 * @return Map<Set<String>, Integer> 支持度大于阈值的频繁项集和支持度map
	 * @throws IOException 
	 */
	private static Map<Set<String>, Integer> assertFP(
			Map<Set<String>, Integer> allKItem) {
		// TODO Auto-generated method stub
		Map<Set<String>, Integer> result = new HashMap<Set<String>, Integer>();
		for(Set<String> kItem : allKItem.keySet()){
			for(String[] data : data){
				boolean flag = true;
				for(String str : kItem){
					for(int i=0; i<data.length; i++){
						if()
					}
				}
				if(flag) allKItem.put(kItem, allKItem.get(kItem) + 1);
			}
			if(allKItem.get(kItem) >= min_sup) {
				result.put(kItem, allKItem.get(kItem));
			}
		}
		return result;
	}
	
	/**检测k项集是否该剪切。由于单调性，必须保证k项集的所有k-1项子集都在preMap中出现，否则就该剪切该k项集
	 * @param preMap k-1项频繁集map
	 * @param set 待检测的k项集
	 * @return boolean 是否该剪切
	 * @throws IOException 
	 */
	private static boolean isNeedCut(Map<Set<String>, Integer> preMap, Set<String> set) {
		// TODO Auto-generated method stub
		boolean flag = false;
		List<Set<String>> subSets = getSubSets(set);
		for(Set<String> subSet : subSets){
			if(!preMap.containsKey(subSet)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**检测两个频繁K项集是否可以连接，连接条件是只有最后一个项不同
	 * @param strA1 k项集1
	 * @param strA1 k项集2
	 * @return boolean 是否可以连接
	 * @throws IOException 
	 */
	private static boolean isCanLink(String[] strA1, String[] strA2) {
		// TODO Auto-generated method stub
		boolean flag = true;
		if(strA1.length != strA2.length){
			return false;
		}else {
			for(int i = 0; i < strA1.length - 1; i++){
				if(!strA1[i].equals(strA2[i])){
					flag = false;
					break;
				}
			}
			if(strA1[strA1.length -1].equals(strA2[strA1.length -1])){
				flag = false;
			}
		}
		return flag;
	}
	
	private static Map<String, Integer> createFrequent1Itemset(ArrayList<String[]> data){
		Map<String, Integer> f1Set = new HashMap<String, Integer>();
		Map<String, Integer> itemCount = new HashMap<String, Integer>();
		Iterator it = data.iterator();
		while(it.hasNext()){
			String[] items = (String[])it.next();
			for(int i=0; i<items.length; i++){
				if(itemCount.containsKey(items[i])){
					itemCount.put(items[i], itemCount.get(items[i])+1);
				}
				else{
					itemCount.put(items[i], 1);
				}
			}
		}
		
		for(Map.Entry<String, Integer> ic : itemCount.entrySet()){
			if(ic.getValue() >= min_sup){
				f1Set.put(ic.getKey(), ic.getValue());
			}
		}
		
		return f1Set;
	}
	
	private static void join(){
		
	}
	
	private static void prune(){
		
	}
	
	private static void readData(){
		try{
			Connection conn = getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = null;
			String query = "select product_id,search_word1,search_word2,search_word3,search_word4,weight from `weighted_trajectory` limit 0, 100";
			
			rs=st.executeQuery(query);
			while(rs.next()){
				String id = rs.getString("product_id");
				String weight = rs.getString("weight");
				String searchword1 = rs.getString("search_word1");
				String searchword2 = rs.getString("search_word2");
				String searchword3 = rs.getString("search_word3");
				String searchword4 = rs.getString("search_word4");
				
				String[] lineItem = new String[6];
				lineItem[0] = id;
				lineItem[1] = weight;
				lineItem[2] = searchword1;
				lineItem[3] = searchword2;
				lineItem[4] = searchword3;
				lineItem[5] = searchword4;
				
				data.add(lineItem);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static Connection getConnection()
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
