package com.xingcloud.util;

import com.google.gson.Gson;
import com.xingcloud.model.Index;
import com.xingcloud.model.Query;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryReader {
    private static Logger logger = Logger.getLogger(QueryReader.class);
    private Gson gson;

    public QueryReader() {
        gson = new Gson();
    }

    public Map<String,Set<Index>> readQueryLog(int dateDistance){
        Map<String, Map<Index, Integer>> indexesMap = new HashMap<String, Map<Index, Integer>>();
        String fileName = Utility.getQueryLogPath(dateDistance);
        File file = new File(fileName);
        logger.info("Read query log from file : " + fileName);
        String line = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                Pair<String, List<Index>> pair = parse(line);
                if (pair != null) {
                    String project = pair.first;
                    List<Index> indexList = pair.second;
                    for (Index index : indexList) {
                        Map<Index, Integer> indexes = indexesMap.get(project);
                        if (indexes != null) {
                            Integer value = indexes.get(index);
                            if (value != null) {
                                value += 1;
                            } else {
                                indexes.put(index, 1);
                            }
                        } else {
                            indexes = new HashMap<Index, Integer>();
                            indexes.put(index, 1);
                            indexesMap.put(project, indexes);
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            logger.error("Read query log from : " + fileName + " failed");
        }
        return Utility.extract(indexesMap);
    }
    public Map<String, Set<Index>> readQueryLog() {
        return readQueryLog(-1);
    }

    private Pair<String, List<Index>> parse(String json) {
        try {
            // \" to "
            json = json.replaceAll("\\\\\"","\"") ;
            Query query = gson.fromJson(json, Query.class);
            return query.toIndexs();
        } catch (Exception e) {
            logger.error("Parse query to indexs failed ," + json);
        }
        return null;

    }

}
