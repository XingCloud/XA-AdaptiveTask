package com.xingcloud.model;

import com.xingcloud.util.DateUtil;
import com.xingcloud.util.Utility;

import com.xingcloud.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class Query {

    private String project_id;
    private String start_time;
    private String end_time;
    private Enumeration.INTERVAL interval;
    private Enumeration.TYPE type;


    private List<Item> items;

    class Item {
        public String name;
        public String event_key;
        public Object segment;
        public int number_of_day;
        public int number_of_day_origin;
        private Enumeration.GROUPBY_TYPE groupby_type;
        private String groupby;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEvent_key() {
            return event_key;
        }

        public void setEvent_key(String event_key) {
            this.event_key = event_key;
        }

        public Object getSegment() {
            return segment;
        }

        public void setSegment(Object segment) {
            this.segment = segment;
        }

        public int getNumber_of_day() {
            return number_of_day;
        }

        public void setNumber_of_day(int number_of_day) {
            this.number_of_day = number_of_day;
        }

        public int getNumber_of_day_origin() {
            return number_of_day_origin;
        }

        public void setNumber_of_day_origin(int number_of_day_origin) {
            this.number_of_day_origin = number_of_day_origin;
        }

        public Enumeration.GROUPBY_TYPE getGroupby_type() {
            return groupby_type;
        }

        public void setGroupby_type(Enumeration.GROUPBY_TYPE groupby_type) {
            this.groupby_type = groupby_type;
        }

        public String getGroupby() {
            return groupby;
        }

        public void setGroupby(String groupby) {
            this.groupby = groupby;
        }
    }

    public Query() {

    }


    public Pair<String, List<Index>> toIndexs() {

        Pair<String, List<Index>> pair = new Pair<String, List<Index>>(project_id, new ArrayList<Index>());
        Index index = null;
        String event_key = null;
        if (items != null) {
            for (Item item : items) {
                // offline
                event_key = item.getEvent_key();
                if (!start_time.equals(end_time)) {
                    item.number_of_day = 0 ;
                    item.number_of_day_origin = 0 ;
                    String eventPattern = Utility.getEventPattern(event_key);
                    if (!"visit.*".equals(eventPattern) || "pay.*".equals(eventPattern))
                        return null;
                    int distance = DateUtil.getDateDistance(start_time, end_time);
                    item.number_of_day -= distance;

                }
                Object segment = item.segment ;
                if (segment == null)
                    segment = "TOTAL_USER";
                switch (type) {
                    case COMMON:
                        index = new Index(interval, event_key, item.number_of_day, item.number_of_day_origin, segment);
                        break;
                    case GROUP:
                        index = new Index(event_key, item.number_of_day, item.number_of_day_origin, segment, item.groupby_type, item.groupby);
                        break;
                }
                pair.second.add(index);

            }
        }
        return pair;

    }
}
