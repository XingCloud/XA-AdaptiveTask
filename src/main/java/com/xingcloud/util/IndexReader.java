package com.xingcloud.util;

import com.google.gson.Gson;
import com.xingcloud.model.Enumeration;
import com.xingcloud.model.Index;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/26/13
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexReader {

    private final  static  int EIGHT_DAYS_AGO = -8 ;
    private Gson gson = null;

    public IndexReader() {
        gson = new Gson();
    }

    public Map<String, Set<Index>> readIndexes(Enumeration.INDEX_LOG_TYPE type) {
        Map<String, Set<Index>> indexesMap = new HashMap<String, Set<Index>>();
        String parentPath = null;
        switch (type) {
            case DAY_LOG:
                parentPath = Utility.getDayIndexLogParentPath(DateUtil.getDateByDistance(EIGHT_DAYS_AGO));
                break;
            case WEEK_LOG:
                parentPath = Utility.getWeekIndexLogParentPath();
                break;
        }
        List<String> projects = FileUtil.listProjects(parentPath);
        for (String project : projects) {
            String fileName = parentPath + project;
            String fileContent = FileUtil.readFileContent(fileName);
            if (fileContent != null) {
                Index[] indexes = gson.fromJson(fileContent, Index[].class);
                indexesMap.put(project, new HashSet<Index>(Arrays.asList(indexes)));
            }
        }
        return indexesMap;
    }
}
