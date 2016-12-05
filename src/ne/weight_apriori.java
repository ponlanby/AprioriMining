package ne;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class weight_apriori {
	private final static float AVG_WEIGHT = (float) 0.22;
	
	private final static float SUPPORT = 2 * AVG_WEIGHT; // 支持度阈值
	private final static double CONFIDENCE = 0.7 * AVG_WEIGHT;
	// 置信度阈值
	private final static String ITEM_SPLIT = ";";
	// 项之间的分隔符
	private final static String CON = "->";

	// 项之间的分隔符

	/**
	 * 算法主程序
	 * 
	 * @param dataList
	 * @return
	 */
	public Map<String, Float> apriori(ArrayList<String> dataList) {
		Map<String, Float> stepFrequentSetMap = new HashMap<>();//候选项集
		stepFrequentSetMap.putAll(findFrequentOneSets(dataList));
		System.out.println("Items: " + stepFrequentSetMap.size());

		Map<String, Float> frequentSetMap = new HashMap<String, Float>();// 频繁项集
		frequentSetMap.putAll(stepFrequentSetMap);//生成频繁1项集

		while (stepFrequentSetMap != null && stepFrequentSetMap.size() > 0) {
			Map<String, Float> candidateSetMap = aprioriGen(stepFrequentSetMap);

			Set<String> candidateKeySet = candidateSetMap.keySet();

			// 扫描D，进行计数
			for (String data : dataList) {
				String[] dataItem = data.split(ITEM_SPLIT);
				float weight = Float.parseFloat(dataItem[dataItem.length-1]);
//				System.out.println(weight);
				for (String candidate : candidateKeySet) {
					boolean flag = true;
					String[] strings = candidate.split(ITEM_SPLIT);
					for (String string : strings) {
						if (data.indexOf(string + ITEM_SPLIT) == -1) {
							flag = false;
							break;
						}
					}
					if (flag)
						candidateSetMap.put(candidate,
								candidateSetMap.get(candidate) + weight);
				}
			}

			// 从候选集中找到符合支持度的频繁项集
			stepFrequentSetMap.clear();
			for (String candidate : candidateKeySet) {
				Float count = candidateSetMap.get(candidate);
				if (count >= SUPPORT)
					stepFrequentSetMap.put(candidate, count);
			}

			// 合并所有频繁集
			frequentSetMap.putAll(stepFrequentSetMap);
		}

		return frequentSetMap;
	}

	/**
	 * find frequent 1 itemsets
	 * 
	 * @param dataList
	 * @return
	 */
	private Map<String, Float> findFrequentOneSets(ArrayList<String> dataList) {
		Map<String, Float> resultSetMap = new HashMap<>();

		for (String data : dataList) {
			String[] strings = data.split(ITEM_SPLIT);
			String w = strings[strings.length-1];
//			System.out.println(strings[strings.length-1]);			float weight = Float.parseFloat(w);
			for(int i=0; i<strings.length-1; i++){
				String string = strings[i] + ";";
				if (resultSetMap.get(string) == null) {
					resultSetMap.put(string, weight);
				} else {
					resultSetMap.put(string, resultSetMap.get(string) + weight);
				}
			}
//			for (String string : strings) {
//				string += ITEM_SPLIT;
//				if (resultSetMap.get(string) == null) {
//					resultSetMap.put(string, 1);
//				} else {
//					resultSetMap.put(string, resultSetMap.get(string) + 1);
//				}
//			}
		}
		return resultSetMap;
	}

	/**
	 * 根据上一步的频繁项集的集合选出候选集
	 * 
	 * @param setMap
	 * @return
	 */
	private Map<String, Float> aprioriGen(Map<String, Float> setMap) {
		Map<String, Float> candidateSetMap = new HashMap<>();

		Set<String> candidateSet = setMap.keySet();
		for (String s1 : candidateSet) {
			String[] strings1 = s1.split(ITEM_SPLIT);
			String s1String = "";
			for (String temp : strings1)
				s1String += temp + ITEM_SPLIT;

			for (String s2 : candidateSet) {
				String[] strings2 = s2.split(ITEM_SPLIT);

				boolean flag = true;
				for (int i = 0; i < strings1.length - 1; i++) {
					if (strings1[i].compareTo(strings2[i]) != 0) {
						flag = false;
						break;
					}
				}
				if (flag
						&& strings1[strings1.length - 1]
								.compareTo(strings2[strings1.length - 1]) < 0) {
					// 连接步：产生候选
					String c = s1String + strings2[strings2.length - 1]
							+ ITEM_SPLIT;
					if (hasInfrequentSubset(c, setMap)) {
						// 剪枝步：删除非频繁的候选
					} else {
						candidateSetMap.put(c, (float) 0.0);
					}
				}
			}
		}

		return candidateSetMap;
	}

	/**
	 * 使用先验知识，判断候选集是否是频繁项集
	 * 
	 * @param candidate
	 * @param setMap
	 * @return
	 */
	private boolean hasInfrequentSubset(String candidateSet,
			Map<String, Float> setMap) {
		String[] strings = candidateSet.split(ITEM_SPLIT);

		// 找出候选集所有的子集，并判断每个子集是否属于频繁子集
		for (int i = 0; i < strings.length; i++) {
			String subString = "";
			for (int j = 0; j < strings.length; j++) {
				if (j != i) {
					subString += strings[j] + ITEM_SPLIT;
				}
			}

			if (setMap.get(subString) == null)
				return true;
		}

		return false;
	}

	/**
	 * 由频繁项集产生关联规则
	 * 
	 * @param frequentSetMap
	 * @return
	 */
	public Map<String, Float> getRelationRules(
			Map<String, Float> frequentSetMap) {
		Map<String, Float> relationsMap = new HashMap<>();
		Set<String> keySet = frequentSetMap.keySet();

		for (String key : keySet) {
			List<String> keySubset = subset(key);
			for (String keySubsetItem : keySubset) {
				// 子集keySubsetItem也是频繁项
				Float count = frequentSetMap.get(keySubsetItem);
				if (count != null) {
					Float confidence = (float) ((1.0 * frequentSetMap.get(key))/ (1.0 * frequentSetMap.get(keySubsetItem)))* AVG_WEIGHT;
					if (confidence > CONFIDENCE)
						relationsMap.put(
								keySubsetItem + CON
										+ expect(key, keySubsetItem),
								confidence);
				}
			}
		}

		return relationsMap;
	}

	/**
	 * 求一个集合所有的非空真子集
	 * 
	 * @param sourceSet
	 * @return 为了以后可以用在其他地方，这里我们不是用递归的方法
	 * 
	 *         参考：http://blog.163.com/xiaohui_1123@126/blog/static/
	 *         3980524020109784356915/
	 *         思路：假设集合S（A,B,C,D），其大小为4，拥有2的4次方个子集，即0-15，二进制表示为0000
	 *         ，0001，...，1111。 对应的子集为空集，{D}，...，{A,B,C,D}。
	 */
	private List<String> subset(String sourceSet) {
		List<String> result = new ArrayList<>();

		String[] strings = sourceSet.split(ITEM_SPLIT);
		// 非空真子集
		for (int i = 1; i < (int) (Math.pow(2, strings.length)) - 1; i++) {
			String item = "";
			String flag = "";
			int ii = i;
			do {
				flag += "" + ii % 2;
				ii = ii / 2;
			} while (ii > 0);
			for (int j = flag.length() - 1; j >= 0; j--) {
				if (flag.charAt(j) == '1') {
					item = strings[j] + ITEM_SPLIT + item;
				}
			}
			result.add(item);
		}

		return result;
	}

	private String expect(String stringA, String stringB) {
		String result = "";

		String[] stringAs = stringA.split(ITEM_SPLIT);
		String[] stringBs = stringB.split(ITEM_SPLIT);

		for (int i = 0; i < stringAs.length; i++) {
			boolean flag = true;
			for (int j = 0; j < stringBs.length; j++) {
				if (stringAs[i].compareTo(stringBs[j]) == 0) {
					flag = false;
					break;
				}
			}
			if (flag)
				result += stringAs[i] + ITEM_SPLIT;
		}

		return result;
	}

	public static void main(String[] args) throws Exception {
		ArrayList<String> dataList = new ArrayList<>();
		long starTime=0;
		long endTime=0;
		long Time=0;
		
		starTime=System.currentTimeMillis();
		String driver = "com.mysql.jdbc.Driver"; // 连接数据库
		Class.forName(driver);
		Connection connecter = DriverManager.getConnection( "jdbc:mysql://localhost:3306/b2b_db?useSSL=false", "root", "trc20183");
		if (!connecter.isClosed())
			System.out.println("success in getConnetion");
		Statement statement = connecter.createStatement();
		String sql = "select search_word1,search_word2,search_word3,search_word4,weight from `weighted_trajectory` limit 0, 1000";
//		String sql = "select search_word1,search_word2,search_word3,search_word4,weight from `weighted_trajectory` where category='Service'";
		ResultSet rs = statement.executeQuery(sql);

		String pw = null;
		String pw2 = null;
		while (rs.next()) {
			String sw1 = rs.getString("search_word1");
			String sw2 = rs.getString("search_word2");
			String sw3 = rs.getString("search_word3");
//			String sw4 = rs.getString("search_word4");
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
//			if(!sw4.equals("null")){
//				pw2 += ITEM_SPLIT + sw4;
//			}
			if(!weight.equals("null")){
				pw2 += ITEM_SPLIT + weight;
			}
//			pw = sw1 + ITEM_SPLIT + sw2 + ITEM_SPLIT + sw3 + ITEM_SPLIT + sw4;
//			System.out.println(pw);
//			System.out.println(pw2);
			dataList.add(pw2);
		}
		endTime=System.currentTimeMillis();
		Time=endTime-starTime;
		System.out.println("Load data: " + Time+ "ms");

//		System.out.println("=数据集合==========");
//		for (String string : dataList) {
//			System.out.println(string);
//		}

		weight_apriori apriori2 = new weight_apriori();

		System.out.println("=频繁项集==========");

		starTime=System.currentTimeMillis();
		Map<String, Float> frequentSetMap = apriori2.apriori(dataList);
		endTime=System.currentTimeMillis();
		Time=endTime-starTime;
		System.out.println("Mining: " + Time+ "ms");
		
		Set<String> keySet = frequentSetMap.keySet();
		System.out.println();
		int count = 0;
		System.out.println("=频繁1项集==========");
		for (String key : keySet) {
			String[] keys =key.split(ITEM_SPLIT);
			if(keys.length==1){
				count++;
				System.out.println(key.substring(0, key.length()-1) + "				支持度：" + frequentSetMap.get(key));
//				System.out.println(key + "				支持度：" + frequentSetMap.get(key));
			}
//			System.out.println(key + " : " + frequentSetMap.get(key));
		}
		System.out.println("频繁1项集共" + count + "项");
		System.out.println();
		count = 0;
		System.out.println("=频繁2项集==========");
		for (String key : keySet) {
			String[] keys =key.split(ITEM_SPLIT);
			if(keys.length==2){
				count++;
				System.out.println(key.substring(0, key.length()-1) + "				支持度：" + frequentSetMap.get(key));
//				System.out.println(key + "				支持度：" + frequentSetMap.get(key));
			}
//			System.out.println(key + " : " + frequentSetMap.get(key));
		}
		System.out.println("频繁2项集共" + count + "项");
		System.out.println();
		count = 0;
		System.out.println("=频繁3项集==========");
		for (String key : keySet) {
			String[] keys =key.split(ITEM_SPLIT);
			if(keys.length==3){
				count++;
				System.out.println(key.substring(0, key.length()-1) + "				支持度：" + frequentSetMap.get(key));
//				System.out.println(key + "				支持度：" + frequentSetMap.get(key));
			}
//			System.out.println(key + " : " + frequentSetMap.get(key));
		}
		System.out.println("频繁3项集共" + count + "项");
		System.out.println();
		count = 0;
		System.out.println("=关联规则==========");
		Map<String, Float> relationRulesMap = apriori2
				.getRelationRules(frequentSetMap);
		Set<String> rrKeySet = relationRulesMap.keySet();
		for (String rrKey : rrKeySet) {
			count++;
			System.out.println(rrKey + "  :  " + relationRulesMap.get(rrKey));
		}
		System.out.println("关联规则共" + count + "项");

	}
}