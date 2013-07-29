package com.xingcloud.exec;

import com.xingcloud.model.Index;
import com.xingcloud.util.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 7/29/13
 * Time: 4:13 PM
 */
public class AggerateAnalyzer {

  private Map<String, Map<Index, Integer>> result = new HashMap<String, Map<Index, Integer>>();

  public AggerateAnalyzer() {

  }

  public void aggerating(Map<String, Set<Index>> indexSets) {
    for (String project : indexSets.keySet()) {
      Map<Index, Integer> indexMapSrc = Utility.Set2Map(indexSets.get(project));
      Map<Index, Integer> indexMapDst = result.get(project);
      if (indexMapDst == null) {
        result.put(project, indexMapSrc);
      } else {
        Utility.MergeMap(indexMapSrc, indexMapDst);
      }
    }
  }

  public Map<String, Set<Index>> getResult() {
    return Utility.extract(result);
  }
}
