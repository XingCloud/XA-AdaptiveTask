package com.xingcloud.util;

import com.xingcloud.model.Index;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utility {
    private static Logger logger = Logger.getLogger(Utility.class) ;

    private final  static String QUERY_LOG_PATH_PREFIX = "/data/catalina/8081/logs/parameters.log."  ;
    private final  static String INDEX_LOG_PATH_PREFIX = "/data/log/queryanalysis/" ;
    private final  static String INDEX_TASK_PATH_PREFIX = "taskIndexes/" ;

    public static String getEventPattern(String event) {
        return event.replaceAll("(\\.\\*)+$", ".*");
    }


    public static String getQueryLogPath() {
        String date = DateUtil.getDateByDistance(-1);
        return getQueryLogPath(date);
    }
    public static  String getQueryLogPath(String date){
        return QUERY_LOG_PATH_PREFIX + date ;
    }

    public static String getDayIndexLogParentPath(String date) {
         return INDEX_LOG_PATH_PREFIX + date + "/" ;
    }

    public static String getDayIndexLogPath(String project,String date){
        return getDayIndexLogParentPath(date) + project ;
    }

    public static String getWeekIndexLogParentPath(){
        return  INDEX_LOG_PATH_PREFIX + "week/" ;
    }
    public static  String getWeekIndexLogPath(String project){
        return  getWeekIndexLogParentPath() + project ;
    }

    public static String getTaskIndexParentPath() {
         return INDEX_TASK_PATH_PREFIX  ;
    }
    public static  String getTaskIndexPath(String project){
        return INDEX_TASK_PATH_PREFIX + project ;
    }


    public static Map<String, Map<Index, Integer>> transform(Map<String, Set<Index>> src) {
        Map<String, Map<Index, Integer>> dst = new HashMap<String, Map<Index, Integer>>();
        for (String key : src.keySet()) {
            Map<Index, Integer> map = new HashMap<Index, Integer>();
            for (Index index : src.get(key)) {
                map.put(index, index.getFreq());
            }
            dst.put(key, map);
        }
        return dst;
    }

    public static Map<String, Set<Index>> extract(Map<String, Map<Index, Integer>> src) {
        Map<String, Set<Index>> dst = new HashMap<String, Set<Index>>();
        for(Map.Entry<String,Map<Index,Integer>> entry : src.entrySet()){
            Map<Index,Integer> values = entry.getValue();
            Object[] indexs = values.keySet().toArray() ;
            for(int i = 0 ; i < indexs.length ; i ++){
                ((Index)indexs[i]).setFreq(values.get(indexs[i]));
            }
            dst.put(entry.getKey(),entry.getValue().keySet()) ;
        }
        return dst ;
    }

    private static List<Index> SortDescendByFreq(Set<Index> indexSet){
        List<Index> indexList = new ArrayList<Index>(indexSet) ;
        Collections.sort(indexList,new Comparator<Index>() {
            @Override
            public int compare(Index left, Index right) {
                return - left.getFreq().compareTo(right.getFreq());
            }
        });
        return indexList ;
    }

    public static Set<Index> TopFreq(Set<Index> indexSet,float percent ,int min){
        int num = 0 ;
        int size = indexSet.size() ;
        int part = (int) (indexSet.size() * percent) ;
        num = size <= min ? size : part <= min ? min : part ;
        logger.info(num + " of " + indexSet.size() + " indexes need to compute today");
        List<Index> sortedList = SortDescendByFreq(indexSet) ;
        return  new HashSet<Index>(sortedList.subList(0,num)) ;


    }


}
