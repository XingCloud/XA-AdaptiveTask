package com.xingcloud.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class Index {

    private Enumeration.INTERVAL interval;
    private String event;
    private Enumeration.TYPE type;
    private int coverRange;
    private int coverRangeOrigin;
    private Object segment;

    // for group by
    Map<String, ArrayList<Object>> group_by;

    private Integer freq;

    public Index() {
    }

    // COMMON
    public Index(Enumeration.INTERVAL interval,
                 String event,
                 int coverRange,
                 int coverRangeOrigin,
                 Object segment) {
        this.interval = interval;
        this.event = event;
        this.type = Enumeration.TYPE.COMMON;
        this.coverRange = coverRange;
        this.coverRangeOrigin = coverRangeOrigin;
        this.segment = segment;
    }

    // GROUP
    public Index(String event,
                 int coverRange,
                 int coverRangeOrigin,
                 Object segment,
                 Enumeration.GROUPBY_TYPE groupby_type,
                 String groupby) {
        this.interval = Enumeration.INTERVAL.DAY;
        this.event = event;
        this.type = Enumeration.TYPE.GROUP;
        this.coverRange = coverRange;
        this.coverRangeOrigin = coverRangeOrigin;
        this.group_by = new TreeMap<String, ArrayList<Object>>();
        this.segment = segment;
        ArrayList<Object> bys = new ArrayList<Object>();

        switch (groupby_type) {
            case USER_PROPERTIES:
                bys.add(groupby);
                group_by.put("USER_RPOPERTIES", bys);
                break;
            case EVENT:
                bys.add(Integer.parseInt(groupby));
                group_by.put("EVENT", bys);
                break;
        }
    }

    public Enumeration.INTERVAL getInterval() {
        return interval;
    }

    public void setInterval(Enumeration.INTERVAL interval) {
        this.interval = interval;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Enumeration.TYPE getType() {
        return type;
    }

    public void setType(Enumeration.TYPE type) {
        this.type = type;
    }

    public int getCoverRange() {
        return coverRange;
    }

    public void setCoverRange(int coverRange) {
        this.coverRange = coverRange;
    }

    public int getCoverRangeOrigin() {
        return coverRangeOrigin;
    }

    public void setCoverRangeOrigin(int coverRangeOrigin) {
        this.coverRangeOrigin = coverRangeOrigin;
    }

    public Object getSegment() {
        return segment;
    }

    public void setSegment(Object segment) {
        this.segment = segment;
    }

    public Map<String, ArrayList<Object>> getGroup_by() {
        return group_by;
    }

    public void setGroup_by(Map<String, ArrayList<Object>> group_by) {
        this.group_by = group_by;
    }

    public Integer getFreq() {
        return freq;
    }

    public void setFreq(Integer freq) {
        this.freq = freq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Index)) return false;

        Index index = (Index) o;

        if (coverRange != index.coverRange) return false;
        if (coverRangeOrigin != index.coverRangeOrigin) return false;
        if (event != null ? !event.equals(index.event) : index.event != null) return false;
        if (group_by != null ? !group_by.equals(index.group_by) : index.group_by != null) return false;
        if (interval != index.interval) return false;
        if (segment != null ? !segment.equals(index.segment) : index.segment != null) return false;
        if (type != index.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = interval != null ? interval.hashCode() : 0;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + coverRange;
        result = 31 * result + coverRangeOrigin;
        result = 31 * result + (segment != null ? segment.hashCode() : 0);
        result = 31 * result + (group_by != null ? group_by.hashCode() : 0);
        return result;
    }
}