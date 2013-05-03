package com.xingcloud.exec;

import com.xingcloud.model.Index;
import com.xingcloud.util.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class RollingAnalyzer {

    private Map<String, Set<Index>> start;
    private Map<String, Set<Index>> window;
    private Map<String, Set<Index>> end;

    public RollingAnalyzer() {
    }

    public RollingAnalyzer(Map<String, Set<Index>> start,
                           Map<String, Set<Index>> window,
                           Map<String, Set<Index>> end) {

        this.start = start;
        this.window = window;
        this.end = end;
    }

    public void rolling() {
        Map<String, Map<Index, Integer>> newWindow = Utility.transform(window);
        for (String key : start.keySet()) {
            Map<Index, Integer> indexs = newWindow.get(key);
            if (indexs != null) {
                for (Index index : start.get(key)) {
                    Integer value = indexs.get(index);
                    if (value != null) {
                        value -= index.getFreq();
                        indexs.remove(index);
                        if (value > 0) {
                            index.setFreq(value);
                            indexs.put(index, value);
                        }
                    }
                }
            }
        }

        for (String key : end.keySet()) {
            Map<Index, Integer> indexs = newWindow.get(key);
            if (indexs != null) {
                for (Index index : end.get(key)) {
                    Integer value = indexs.get(index);
                    if (value != null) {
                        indexs.remove(index);
                        value += index.getFreq();
                        index.setFreq(value);
                    }
                    indexs.put(index, index.getFreq());
                }
            } else {
                Map<Index, Integer> map = new HashMap<Index, Integer>();
                for (Index index : end.get(key)) {
                    map.put(index, index.getFreq());
                }
                newWindow.put(key, map);
            }
        }
        window = Utility.extract(newWindow);
    }


    public Map<String, Set<Index>> getStart() {
        return start;
    }

    public void setStart(Map<String, Set<Index>> start) {
        this.start = start;
    }

    public Map<String, Set<Index>> getWindow() {
        return window;
    }

    public void setWindow(Map<String, Set<Index>> window) {
        this.window = window;
    }

    public Map<String, Set<Index>> getEnd() {
        return end;
    }

    public void setEnd(Map<String, Set<Index>> end) {
        this.end = end;
    }
}
