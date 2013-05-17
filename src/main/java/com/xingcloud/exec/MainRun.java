package com.xingcloud.exec;

import com.xingcloud.model.*;
import com.xingcloud.model.Enumeration;
import com.xingcloud.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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
    static {
        PropertyConfigurator.configure(MainRun.class.getResource("/log4j.properties"));
    }

    private static Logger logger = Logger.getLogger(MainRun.class);

    public static void main(String args[]) {

        // Day Rolling Analysis Default
        if (args.length == 1 && args[0].equals("weekAnalysis")) {
            weekAnalysis();
        } else {
            dayRollingAnalysis();
        }
    }

    private static void dayRollingAnalysis() {
        logger.info("Start day rolling analysis");
        IndexReader indexReader = new IndexReader();
        IndexWriter indexWriter = new IndexWriter();
        QueryReader queryReader = new QueryReader();

        Map<String, Set<Index>> beforeWeekStatis = indexReader.readIndexes(Enumeration.INDEX_LOG_TYPE.DAY_LOG);
        Map<String, Set<Index>> weekStatis = indexReader.readIndexes(Enumeration.INDEX_LOG_TYPE.WEEK_LOG);
        Map<String, Set<Index>> yesterDayStatis = queryReader.readQueryLog();

        RollingAnalyzer analyzer = new RollingAnalyzer();
        analyzer.setStart(beforeWeekStatis);
        analyzer.setWindow(weekStatis);
        analyzer.setEnd(yesterDayStatis);
        analyzer.rolling();

        weekStatis = analyzer.getWindow();

        // store log
        logger.info("Store log");
        FileUtil.deleteLocalDir(Utility.getWeekIndexLogParentPath());
        indexWriter.writeIndexes(weekStatis, Enumeration.INDEX_LOG_TYPE.WEEK_LOG);
        indexWriter.writeIndexes(yesterDayStatis, Enumeration.INDEX_LOG_TYPE.DAY_LOG);

        // write task
        logger.info("Write task");
        FileUtil.deleteHDFSDir(Utility.INDEX_TASK_PATH_PREFIX);
        Map<String, Set<Index>> taskIndexs = new HashMap<String, Set<Index>>();
        for (Map.Entry<String, Set<Index>> entry : weekStatis.entrySet()) {
            taskIndexs.put(entry.getKey(), Utility.TopFreq(entry.getValue(), 1.0f, 100));
        }
        indexWriter.writeIndexes(taskIndexs, Enumeration.INDEX_LOG_TYPE.TASK_LOG);
        logger.info("End day rolling analysis");
    }

    private static void weekAnalysis() {
        logger.info("Start week analysis");
        QueryReader queryReader = new QueryReader();
        IndexWriter indexWriter = new IndexWriter();

        Map<String, Set<Index>> beforeWeekStatis = new HashMap<String, Set<Index>>();
        Map<String, Set<Index>> weekStatis = new HashMap<String, Set<Index>>();
        Map<String, Set<Index>> dayStatis = null;

        RollingAnalyzer analyzer = new RollingAnalyzer();
        analyzer.setStart(beforeWeekStatis);
        analyzer.setWindow(weekStatis);

        for (int i = 1; i < 8; i++) {
            dayStatis = queryReader.readQueryLog(-i);
            analyzer.setEnd(dayStatis);
            analyzer.rolling();
            FileUtil.deleteLocalDir(Utility.getDayIndexLogParentPath(DateUtil.getDateByDistance(-i)));
            indexWriter.writeIndexes(dayStatis, -i);
        }
        // store log
        weekStatis = analyzer.getWindow();
        FileUtil.deleteLocalDir(Utility.getWeekIndexLogParentPath());
        indexWriter.writeIndexes(weekStatis, Enumeration.INDEX_LOG_TYPE.WEEK_LOG);

        // write task
        logger.info("Write task");
        FileUtil.deleteHDFSDir(Utility.INDEX_TASK_PATH_PREFIX);
        Map<String, Set<Index>> taskIndexs = new HashMap<String, Set<Index>>();
        for (Map.Entry<String, Set<Index>> entry : weekStatis.entrySet()) {
            taskIndexs.put(entry.getKey(), Utility.TopFreq(entry.getValue(), 0.7f, 100));
        }
        indexWriter.writeIndexes(taskIndexs, Enumeration.INDEX_LOG_TYPE.TASK_LOG);

        logger.info("End week analysis");

    }
}
