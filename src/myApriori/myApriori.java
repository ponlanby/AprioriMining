package myApriori;

//Aprioiri 
import java.util.Iterator;
import java.util.List ;
import java.util.Map ;
import java.util.ArrayList ;
import java.util.HashMap; 
import java.util.Map.Entry;
import java.util.Set;
 



public class myApriori {

        static private boolean endTag = false ;
        static private List<List<String>> cItemset ;
        static private List<List<String>> ckItemset ;
        static private Map<List<String>, Integer> lItemset ;
        static private Map<List<String>,Integer>  lkItemset ;
        
        static List<List<String>> record = new ArrayList<List<String>> () ;
        
        final static double MIN_SUPPORT = 2 ;
        static Map<List<String>, Double > confItemset = new HashMap<List<String>,Double >() ;
        
        
        public static  List<List<String>> getDataSet ()
        {
                return FileReader.getDatabase();
        }
        
        
        public static List<List<String>> getFirstCandidate ()
        {
                
                List<List<String>> cItemset = new ArrayList<List<String>> () ;
                List<String> tempLine = new ArrayList<String>() ;
                
                for( int i = 0 ; i < record.size() ; i++ )
                {
                        
                        for (int j = 0 ; j < record.get(i).size(); j++)
                        {
                                if(tempLine.contains(record.get(i).get(j))) ;
                                else
                                {
                                        tempLine.add(record.get(i).get(j)) ;
                                         
                                }
                        }
                        
                }
                
                for ( int i = 0 ; i < tempLine.size() ;i++)
                {
                        List<String> str = new ArrayList<String>() ;
                        str.add(tempLine.get(i));
                        
                        cItemset.add(str) ;
                         
                }
                
                return cItemset ;
        }
        
        static Map<List<String>,Integer> getSupportedItemset( List<List<String>> cItemset )
        {
                Map<List<String>,Integer> supportedItemset = new HashMap<List<String>,Integer> () ;
                
                boolean end = true ;
                
                for( int i = 0 ; i < cItemset.size(); i++ )
                {
                        int count = countFrequent ( cItemset.get(i)) ;
                        
                        if( count >= MIN_SUPPORT )
                        {
                        supportedItemset.put(cItemset.get(i), count) ;
                        end = false ;
                
                        }
                }
                
                endTag = end ;
                
                
                //System.out.println(\"value of the endTag here !!!\"+endTag);
                
                return supportedItemset  ;
        }
        
        static int countFrequent ( List<String> list)
        {
                
                int count = 0 ;
                
                for ( int i = 1 ; i < record.size() ; i++ )
                {
                        boolean curRecordLineNotHave = false ;
                        
                        for ( int k = 0 ; k < list.size(); k++)
                        {
                                if(!record.get(i).contains(list.get(k)))
                                {
                                        curRecordLineNotHave = true ;
                                        break ;
                                }
                        }
                        
                        if(curRecordLineNotHave == false )
                        {
                                count++ ;
                        }
                }
                
                return count ;
        }
        
        
        /**
         * method following is the getNextCandidata usually can be known as 
         * get Ck from Lk-1 
         * */
        private static List<List<String>> getNextCandidate ( Map<List<String>,Integer> lItemset )
        {
                List<List<String>> nextItemset = new ArrayList<List<String>>() ;
                
                List<List<String>> preItemset = getLItemset(lItemset ) ;
                
                int count = 0 ;
                
                for ( int i = 0 ; i < preItemset.size() ; i++ )
                {
                        List<String> tempLine = new ArrayList<String> () ;
                        tempLine.addAll(preItemset.get(i)) ;
                   
                        for( int j = i+1 ; j < preItemset.size(); j++)
                        {
                                 if( preItemset.get(i).size() == preItemset.get(j).size())
                                 {
                                                              
                                         if( 1 == differElemNum(preItemset.get(i),preItemset.get(j)))
                                         {
                                                 int index = getDifferIndex ( preItemset.get(i), preItemset.get(j)) ;
                                                 
                       
                                                 tempLine.add(preItemset.get(j).get(index)) ;
                                                 
                     
                                                 if( isSubSets ( tempLine, preItemset))
                                                 {
                                                                                       
                                                         List<String> aLine = new ArrayList() ;
                                                         
                                                         for(int m = 0 ; m < tempLine.size() ;m++)
                                                         {
                                                                 aLine.add(tempLine.get(m));
                                                         }
                                                         
                                                         if( nextItemSetNotHave( aLine, nextItemset ))
                                                                 nextItemset.add(aLine) ;
                                                         
                                                 }
                                         }
                                 }//outer if 
                                 
                                 tempLine.remove(tempLine.size()-1 ) ;
                        }//for j 
                }
                                
                 return nextItemset ;
                                                 
        }
        
        
        private static boolean nextItemSetNotHave( List<String> aLine , List<List<String>> nextItemset )
        {
                boolean notHave = true ;
                
                for( int i = 0 ; i < nextItemset.size(); i++ )
                {
                        if(aLine.equals(nextItemset.get(i)))
                        {
                                notHave = false ;
                        }
                }
                
                return notHave ;
        }
        
        
        private static int getDifferIndex ( List<String> list1 , List<String> list2)
        {
                int index = -1 ;
                
                for ( int i = 0 ; i < list1.size() ; i++ )
                {
                        for( int j = 0 ; j < list1.size(); j++ )
                        {
                                if ( !list1.get(i).equals(list2.get(j)))
                                {
                                        index = j ;
                                }
                        }
                        
                        if ( index != -1 )
                                break ;
                }
                
                return index ;
        }
        
        private static int differElemNum ( List<String> list1, List<String>list2 )
        {
                int count = 0 ;
                
                boolean flag ;
                
                for( int i = 0 ; i < list1.size() ; i++ )
                {
                        flag = true ;
                        
                        for(int j = 0 ; j < list1.size(); j++ )
                        {
                                if(list1.get(i).equals(list2.get(j)))
                                {
                                         flag = false ;
                                        break;
                                }
                        }
                        
                        if( flag == true )
                        {
                                count++ ;
                        }
                }
                
                return count ;
        }
         
        
        
        /**
         * method following is used to justice whether 
         * @param tempList all subsets except itself is the subsets of 
         * @param lItemset
         * 
         * @return boolean true : all subsets of tempList are all in
         * lItemset\'s set
         * */
        
        private static boolean isSubSets ( List<String> tempList , List<List<String>> lItemset)
        {
                
                boolean flag = false ;
                
                for ( int i = 0 ; i < tempList.size() ; i++ )
                {
                        List<String> testLine = new ArrayList() ;
                        
                        for (int j = 0 ; j < tempList.size(); j++ )
                        {
                                if (i!= j )
                                {
                                        testLine.add(tempList.get(j)) ;
                                }
                        }
                        
                         for ( int k = 0 ; k < lItemset.size() ; k++ )
                        {
                                if ( testLine.equals(lItemset.get(k)))
                                {
                                        flag = true ;
                                        break ;
                                }
                        }
                        
                        
                        if ( flag == false )
                        {
                              
                                return false ;
                        }
                }
                
                
                return flag ; //return true ;
                
        }
              
        
        private static List<List<String>> getLItemset ( Map<List<String>, Integer> lItemset )
        {
                List<List<String>> itemset = new ArrayList<List<String>> () ;
                
                Iterator<Map.Entry<List<String>, Integer>> iterator = lItemset.entrySet().iterator();
                Entry<List<String>, Integer> entry ;
                
                
                while ( iterator.hasNext() )
                {
                        entry = iterator.next();
                        List<String> key = entry.getKey() ;
                        
                        itemset.add(key) ;
                        
                         
                }
                return itemset ;
        }
        
        public static void main ( String [] args ) throws Exception 
        {
                record =getDataSet() ;
                
                
         
                 cItemset = getFirstCandidate() ;
                
                lItemset = getSupportedItemset( cItemset ) ;
                
                 printfLKitemset ( lItemset) ;
                
                
                
                  while ( endTag != true )
                 {
                         ckItemset = getNextCandidate(lItemset ) ;
                         lkItemset = getSupportedItemset ( ckItemset ) ;
                         
                         if(lkItemset.size() != 0 )
                                 printfLKitemset ( lkItemset) ;
                         
                         cItemset = ckItemset ;
                         lItemset = lkItemset ;
                 }  
                 
                  System.out.println("finish ") ;
                 
                 
        }
        
         private static void printfLKitemset ( Map<List<String> , Integer> lkItemset )
         {
                 Iterator<Entry<List<String>,Integer>> iterator = lkItemset.entrySet().iterator();
                 
                 Entry<List<String>,Integer> entry ;
                 
                 while ( iterator.hasNext() )
                 {
                         entry = iterator.next() ;
                         
                         List<String> key = entry.getKey() ;
                         Integer value = entry.getValue() ;
                         
                         System.out.println("the key : ");
                         
                         for ( int i = 0 ; i < key.size() ; i++ )
                         {
                                 System.out.print(key.get(i));
                                 System.out.print("  "); 
                         }
                         
                         System.out.println("the value : "+ value.intValue());
                         
                 }
         }
        
        
        
}
