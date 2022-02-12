package lj.study.test.tmp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestString {

    @Test
    public void test_Optional(){
        JSONObject json = new JSONObject();
        json.put("k1", "v1");
        json.put("k2", "v2");
        System.out.println(json.toJSONString());

        JSONPath.set(json, "$.ka", new JSONArray());
        JSONPath.set(json, "$.kj", new JSONObject());
        json.remove("k1");
        json.remove("k3");
        System.out.println(json.toJSONString());
    }

    @Test
    public void testEquals(){
        String s1 = "abc";
        String s2 = "abc";
        if(s1 == s2){
            System.out.println("== true");
        }
        if(s1.equals(s2)){
            System.out.println("equals true");
        }

        String s_n_1 = new String("abc");
        String s_n_2 = new String("abc");
        if(s_n_1 == s_n_2){
            System.out.println("== true");
        }
        if(s_n_1.equals(s_n_2)){
            System.out.println("equals true");
        }

        if(s1 == s_n_1){
            System.out.println(" == true");
        }
    }

    @Test
    public void testSub(){
        JSONArray idMapping = JSON.parseArray("[\n" +
                "    {\n" +
                "        \"modelColumn\":\"idY_match\",\n" +
                "        \"originalModelColumn\":\"idY_match\",\n" +
                "        \"isId\":1\n" +
                "    }\n" +
                "]");
        System.out.println(idMapping.size());
        System.out.println(idMapping.getJSONObject(0).keySet());
    }

}
