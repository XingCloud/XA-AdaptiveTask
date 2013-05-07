package com.xingcloud.util;

import com.google.gson.Gson;
import com.xingcloud.model.*;
import com.xingcloud.model.Enumeration;

import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexWriter {

    private static final int YESTERDAY = -1 ;
    private Gson gson = null;

    public IndexWriter() {
        gson = new Gson();
    }

    public void writeIndexes(Map<String,Set<Index>> indexesMap,Enumeration.INDEX_LOG_TYPE type){
         writeIndexes(indexesMap, type,YESTERDAY);
    }

    public void writeIndexes(Map<String,Set<Index>> indexesMap,int dateDistance){
        writeIndexes(indexesMap, Enumeration.INDEX_LOG_TYPE.DAY_LOG,dateDistance);
    }

    private void writeIndexes(Map<String,Set<Index>> indexesMap,Enumeration.INDEX_LOG_TYPE type, int dateDistance){
        String partentPath = null ;
        FileUtil.FST fsType  = FileUtil.FST.LOCAL ;
        switch (type){
            case DAY_LOG:
                partentPath = Utility.getDayIndexLogParentPath(DateUtil.getDateByDistance(dateDistance));
                break ;
            case WEEK_LOG:
                partentPath = Utility.getWeekIndexLogParentPath();
                break;
            case TASK_LOG:
                fsType = FileUtil.FST.HDFS ;
                partentPath = Utility.getTaskIndexParentPath();
        }
        String fileName = null ;
        String content = null ;
        for(Map.Entry<String,Set<Index>> entry : indexesMap.entrySet()){
            fileName = partentPath + entry.getKey() ;
            content = gson.toJson(entry.getValue());
            FileUtil.writeContentToFile(fileName,content,fsType);
        }
    }





}
