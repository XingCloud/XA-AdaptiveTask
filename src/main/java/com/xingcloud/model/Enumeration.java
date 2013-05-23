package com.xingcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Enumeration {
    public enum INTERVAL{
        MIN5,HOUR,DAY,WEEK,MONTH,PERIOD
    }

    public enum TYPE{
        COMMON,GROUP
    }

    public enum GROUPBY_TYPE{
        USER_PROPERTIES,EVENT
    }

    public enum INDEX_LOG_TYPE{
        DAY_LOG,WEEK_LOG,TASK_LOG
    }
}
