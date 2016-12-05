package DataMining_Apriori;

public class Client {
	public static void main(String[] args){
		String filePath = "C:\\Users\\胖子\\Documents\\Eclipse\\ap\\src\\DataMining_Apriori\\testInput.txt";
		
		long starTime=System.currentTimeMillis();
		AprioriTool tool = new AprioriTool(filePath, 3);
		tool.printAttachRule(0.7);
		
		long endTime=System.currentTimeMillis();
		long Time=endTime-starTime;
		System.out.println(Time+ "ms");
	}
}