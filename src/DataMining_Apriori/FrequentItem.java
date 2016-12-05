package DataMining_Apriori;

/**
 * 频繁项集
 * 
 * @author 
 * 
 */
public class FrequentItem implements Comparable<FrequentItem>{
	// 频繁项集的集合ID
	private String[] idArray;
	// 频繁项集的支持度计数
	private int count;
	//频繁项集的长度，1项集或是2项集，亦或是3项集
	private int length;
	
	public FrequentItem(String[] idArray, int count){
		this.idArray = idArray;
		this.count = count;
		length = idArray.length;
	}

	public String[] getIdArray() {
		return idArray;
	}

	public void setIdArray(String[] idArray) {
		this.idArray = idArray;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public int compareTo(FrequentItem o) {
		// TODO Auto-generated method stub
		int result=0;
		String str1=null;
		String str2=null;
		try{
			str1 = this.getIdArray()[0];
			str2 = o.getIdArray()[0];
			if(str1!=null && str2!=null){
				result = str1.compareTo(str2);				
			}
			else if(str1==null && str2!=null){
				result = -1;
			}
			else if(str2==null && str1!=null){
				result = 1;
			}
			else{
				result = 0;
			}
		}
		catch(Exception e){
			System.out.println(str1);
			System.out.println(str2);
			e.printStackTrace();
		}

		
		return result;
	}
	
}