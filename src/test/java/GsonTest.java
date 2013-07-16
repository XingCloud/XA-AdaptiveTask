import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xingcloud.model.*;
import com.xingcloud.model.Enumeration;
import com.xingcloud.util.DateUtil;
import com.xingcloud.util.Pair;
import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class GsonTest {
    // @Test
    public void testGson() {

        Gson gson = new GsonBuilder().create();
        Index common = new Index(Enumeration.INTERVAL.DAY, "visit.*", 0, 0, "TOTAL_USER");
        Index event = new Index("visit.*", 0, 0, "TOTAL_USER", Enumeration.GROUPBY_TYPE.EVENT, "1");
        Index user_properties = new Index("visit.*", 0, 0, "TOTAL_USER", com.xingcloud.model.Enumeration.GROUPBY_TYPE.USER_PROPERTIES, "1");

        // Object
        System.out.println(common);
        System.out.println(gson.toJson(event));
        System.out.println(gson.toJson(user_properties));

        // set
        Set<Object> set = new HashSet<Object>();
        set.add(common);
        set.add(event);
        set.add(user_properties);
        System.out.println(gson.toJson(set));

        // arr
        List<Object> arr = new ArrayList<Object>();
        arr.add(common);
        arr.add(event);
        arr.add(user_properties);
        System.out.println(gson.toJson(arr));


        // form json
        String json = "[{\"interval\":\"DAY\" ,\"event\":\"visit.*.*.*.*.*\",\"type\":\"GROUP\",\"coverRange\":22\n" +
                ",\"coverRangeOrigin\":0,\"segment\":\"TOTAL_USER\",\"group_by\":{\"EVENT\":[0]},\"freq\":1}]";
        Index[] index = gson.fromJson(json, Index[].class);
        List<Index> indexs = Arrays.asList(index);
        System.out.println(gson.toJson(indexs));

    }

    @Test
    public void testP() {
        List<String> indexes = new ArrayList<String>() {
            {
                add("GROUP,minigames337,2013-07-05,2013-07-11,pay.*,{\"register_time\":{\"$gte\":\"2013-07-05\",\"$lte\":\"2013-07-05\"}},VF-ALL-0-0,USER_PROPERTIES,identifier");
                add("GROUP,minigames337,2013-07-05,2013-07-05,pay.*,{\"register_time\":{\"$gte\":\"2013-07-05\",\"$lte\":\"2013-07-05\"}},VF-ALL-0-0,USER_PROPERTIES,identifier");
                add("GROUP,minigames337,2013-07-11,2013-07-11,visit.*,TOTAL_USER,VF-ALL-0-0,USER_PROPERTIES,identifier");
                add("COMMON,minigames337,2013-07-14,2013-07-14,pay.*,{\"first_pay_time\":{\"$gte\":\"2013-07-14\",\"$lte\":\"2013-07-14\"}},VF-ALL-0-0,PERIOD");
                add("COMMON,minigames337,2013-07-14,2013-07-14,pay.*,{\"register_time\":{\"$gte\":\"2013-07-12\",\"$lte\":\"2013-07-12\"}},VF-ALL-0-0,PERIOD");
            }
        };
        for (String str : indexes) {
            parse(str);
        }
    }

    public void parse(String json) {
        try {

            Gson gson = new GsonBuilder().create();
            String[] fileds = json.split(",");
            int length = fileds.length;
            String type = fileds[0];
            String project = fileds[1];
            String startDate = fileds[2];
            String endDate = fileds[3];
            String event = fileds[4];
            String segment = "";
            int coverRange = -DateUtil.getDateDistance(startDate, endDate);
            int coverRangeOrigin = 0;
            int distance = DateUtil.getDateDistance(endDate, DateUtil.getDateByDistance(-1));
            Index index = null;
            boolean first = true;

            if ("GROUP".equals(type)) {
                String gbv = fileds[length - 1];
                Enumeration.GROUPBY_TYPE gbt = fileds[length - 2].equals("EVENT") ? Enumeration.GROUPBY_TYPE.EVENT : Enumeration.GROUPBY_TYPE.USER_PROPERTIES;
                for (int i = 5; i < length - 3; i++) {
                    if (first) {
                        first = false;
                    } else {
                        segment += ",";
                    }
                    segment += fileds[i];
                }

                index = new Index(event, coverRange, coverRangeOrigin, processSegment(distance, segment), gbt, gbv);

            } else {
                String interval = fileds[length - 1];
                for (int i = 5; i < length - 2; i++) {
                    if (first) {
                        first = false;
                    } else {
                        segment += ",";
                    }
                    segment += fileds[i];
                }
                Enumeration.INTERVAL intervalenum = null;
                if ("DAY".equals(interval)) {
                    intervalenum = Enumeration.INTERVAL.DAY;
                } else if ("MIN5".equals(interval)) {
                    intervalenum = Enumeration.INTERVAL.MIN5;
                } else if ("WEEK".equals(interval)) {
                    intervalenum = Enumeration.INTERVAL.WEEK;
                } else if ("PERIOD".equals(interval)) {
                    intervalenum = Enumeration.INTERVAL.DAY;
                }

                index = new Index(intervalenum, event, coverRange, coverRangeOrigin, processSegment(distance, segment));

            }
            System.out.println(gson.toJson(index));


        } catch (Exception e) {
        }

    }

    private Object processSegment(int distance, String segmentStr) {

        Gson gson = new GsonBuilder().create();
        List<String> times = new ArrayList<String>() {
            {
                add("first_pay_time");
                add("register_time");
            }
        };

        if (!segmentStr.startsWith("{"))
            return segmentStr;

        String yesterday = DateUtil.getDateByDistance(-1);
        Map segment = gson.fromJson(segmentStr, Map.class);
        for (String str : times) {
            if (segment.containsKey(str)) {
                if (segment.get(str) instanceof Map) {
                    Map<String, String> map = (Map) segment.get(str);
                    if (map.containsKey("$gte") && map.containsKey("$lte")) {
                        String start = map.get("$gte");
                        String end = map.get("$lte");
                        int innerDistance = DateUtil.getDateDistance(end, yesterday);
                        int seDistance = DateUtil.getDateDistance(start, end);
                        end = DateUtil.getDateByDistance(yesterday, distance - innerDistance);
                        start = DateUtil.getDateByDistance(end, -seDistance);
                        map.put("$gte", start);
                        map.put("$lte", end);

                    }
                }
            }
        }
        return segment;

    }

}
