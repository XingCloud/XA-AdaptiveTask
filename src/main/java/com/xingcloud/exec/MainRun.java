package com.xingcloud.exec;
import com.xingcloud.model.*;
import com.xingcloud.model.Enumeration;
import com.xingcloud.util.IndexReader;
import com.xingcloud.util.IndexWriter;
import com.xingcloud.util.QueryReader;
import com.xingcloud.util.Utility;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainRun {
    private static Logger logger = Logger.getLogger(MainRun.class) ;
    public static void main(String args[]){

        logger.info("Start analysis");
        IndexReader indexReader = new IndexReader() ;
        IndexWriter indexWriter = new IndexWriter();
        QueryReader queryReader = new QueryReader() ;

        Map<String, Set<Index>> beforeWeekStatis = indexReader.readIndexes(Enumeration.INDEX_LOG_TYPE.DAY_LOG);
        Map<String, Set<Index>> weekStatis = indexReader.readIndexes(Enumeration.INDEX_LOG_TYPE.WEEK_LOG) ;
        Map<String, Set<Index>> yesterDayStatis = queryReader.readQueryLog();

        RollingAnalyzer analyzer = new RollingAnalyzer() ;
        analyzer.setStart(beforeWeekStatis);
        analyzer.setWindow(weekStatis);
        analyzer.setEnd(yesterDayStatis);
        analyzer.rolling();

        weekStatis = analyzer.getWindow();

        // store log
        logger.info("Store log");
        indexWriter.writeIndexes(weekStatis, Enumeration.INDEX_LOG_TYPE.WEEK_LOG);
        indexWriter.writeIndexes(yesterDayStatis, Enumeration.INDEX_LOG_TYPE.DAY_LOG);

        // write task
        logger.info("Write task");
        Map<String,Set<Index>> taskIndexs = new HashMap<String, Set<Index>>() ;
        for(Map.Entry<String,Set<Index>> entry : weekStatis.entrySet()){
            taskIndexs.put(entry.getKey(), Utility.TopFreq(entry.getValue(),0.7f,100)) ;
        }
        indexWriter.writeIndexes(taskIndexs, Enumeration.INDEX_LOG_TYPE.TASK_LOG);
        logger.info("End analysis");
    }
}
