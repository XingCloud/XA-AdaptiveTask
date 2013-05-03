import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xingcloud.model.*;
import com.xingcloud.model.Enumeration;
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
    @Test
    public void testGson() {

        Gson gson = new GsonBuilder().create() ;




        Index common = new Index(Enumeration.INTERVAL.DAY,"visit.*", 0, 0,"TOTAL_USER");
        Index event = new Index("visit.*",0,0,"TOTAL_USER", Enumeration.GROUPBY_TYPE.EVENT,"1");
        Index user_properties = new Index("visit.*",0,0,"TOTAL_USER", com.xingcloud.model.Enumeration.GROUPBY_TYPE.USER_PROPERTIES,"1");

        // Object
        System.out.println(common);
        System.out.println(gson.toJson(event));
        System.out.println(gson.toJson(user_properties));

        // set
        Set<Object> set  = new HashSet<Object>();
        set.add(common) ;
        set.add(event) ;
        set.add(user_properties);
        System.out.println(gson.toJson(set));

        // arr
        List<Object> arr = new ArrayList<Object>();
        arr.add(common);
        arr.add(event);
        arr.add(user_properties);
        System.out.println(gson.toJson(arr));


        // form json
        String json =  "[{\"interval\":\"DAY\" ,\"event\":\"visit.*.*.*.*.*\",\"type\":\"GROUP\",\"coverRange\":22\n" +
                ",\"coverRangeOrigin\":0,\"segment\":\"TOTAL_USER\",\"group_by\":{\"EVENT\":[0]},\"freq\":1}]" ;
                Index[] index = gson.fromJson(json,Index[].class) ;
        List<Index> indexs = Arrays.asList(index);
        System.out.println(gson.toJson(indexs));

    }


}
