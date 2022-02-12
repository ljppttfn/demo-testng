package lj.study.utils;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by lijing on 2017/8/29.
 * Desc: 封装了一些针对json的操作，如json比较等
 */
public class JsonUtil {


    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String sortJSONKey(String data) {
        String result = data;
        try {
            JSONObject jsonData = JSONObject.parseObject(data);
            Object o = sortJSONValue(jsonData);

            result = o instanceof JSONObject ? (JSONObject.toJSONString(o)) : String.valueOf(o);
        } catch (Throwable t) {
            // ignore
        }

        return result;
    }

    private static Object sortJSONValue(Object value) {
        if (value instanceof JSONArray) {
            JSONArray arrays = (JSONArray) value;
            JSONArray res = new JSONArray();
            for (Object array : arrays) {
                res.add(sortJSONValue(array));
            }
            value = res;
        } else if (value instanceof JSONObject) {
            value = toSortedMap((JSONObject) value);
        }

        return value;
    }

    private static TreeMap<String, Object> toSortedMap(JSONObject data) {
        TreeMap<String, Object> res = JSON.parseObject(data.toJSONString(),
                new TypeReference<TreeMap<String, Object>>() {
                });

        for (String key : res.keySet()) {
            Object value = res.get(key);
            res.put(key, sortJSONValue(value));
        }

        return res;
    }


    public static void sortJson(JSONObject json) {
        Set<String> keySet = json.keySet();
        for (String key : keySet) {
            if (json.get(key) instanceof JSONObject) {
                sortJson(json.getJSONObject(key));
            } else if (json.get(key) instanceof JSONArray) {
                sortJsonArray(json.getJSONArray(key));
            }
        }
    }


    public static void sortJsonArray(JSONArray jsonArray) {

        List<String> list_orgID = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {

            // 嵌套循环
            sortJson(jsonArray.getJSONObject(i));

            // fastjson 默认不输出为null的字段，及SerializerFeature.WriteMapNullValue默认为false，
            // 如果要输出，需要将 SerializerFeature.WriteMapNullValue
            String str = JSONObject.toJSONString(jsonArray.getJSONObject(i), SerializerFeature.WriteMapNullValue);
            list_orgID.add(str);
        }

        Collections.sort(list_orgID);

        jsonArray.clear();
        for (int i = 0; i < list_orgID.size(); i++) {
            jsonArray.add(JSONObject.parse(list_orgID.get(i)));
        }

    }


    private static boolean compareObject(Object expected, Object actual) {
        try {
            if (!String.valueOf(expected).equalsIgnoreCase(String.valueOf(actual))) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 比较json对象的response
     * 模糊匹配，即当预期结果的每个key对应的value包含在（非完全相等）实际返回的结果中，返回true；
     * 有任意一个key不满足时，返回false。
     *
     * @param expected : 预期结果的response， Json类型
     * @param actual   : 实际返回的response， Json类型
     * @return :模糊匹配的结果
     */
    public static boolean compareJSON(JSONObject expected, JSONObject actual) {

        List<String> list = parserKeyPath(expected);
        logger.info(list.toString());
        for (String path : list) {
            try {
                Object value_expected = JSONPath.eval(expected, path);
                Object value_actual = JSONPath.eval(actual, path);

                boolean temp = compareObject(value_actual, value_expected);
                if (!temp) {
                    logger.info(" >>>>>> 不匹配的 JsonPath： " + path);
                    logger.info(" >>>>>> 此path下的预期结果： " + value_expected);
                    logger.info(" >>>>>> 此path下的实际结果： " + value_actual);
                    return false;
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
                return false;
            }
        }

        return true;
    }

    public static List<String> compareJSONDetail(JSONObject expected, JSONObject actual) {
        List<String> result = new ArrayList<>();

        List<String> list = parserKeyPath(expected);
        logger.info(list.toString());
        for (String path : list) {
            try {
                Object value_expected = JSONPath.eval(expected, path);
                Object value_actual = JSONPath.eval(actual, path);

                boolean temp = compareObject(value_actual, value_expected);
                if (!temp) {
                    logger.info(" >>>>>> 不匹配的 JsonPath： " + path);
                    logger.info(" >>>>>> 此path下的预期结果： " + value_expected);
                    logger.info(" >>>>>> 此path下的实际结果： " + value_actual);
                    String str_tmp = String.format("不匹配的JsonPath：%s, 预期值：%s, 实际值：%s", path, value_expected, value_actual);
                    result.add(str_tmp);
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
                return result;
            }
        }

        return result;
    }

    /**
     * 获取预期response中包含的所有的JsonPath list
     *
     * @param jsonObject :
     * @param keySuper   : 递归传递上一级的  JsonPath
     * @param list       : 存储所有的path
     * @return ：
     */
    private static List<String> parserKeyPath(JSONObject jsonObject, String keySuper, List<String> list) {
        for (String key : jsonObject.keySet()) {
            String path = key;
            if (!keySuper.equals("")) {
                path = keySuper + "." + key; // path 深度递加
            }

            if (jsonObject.get(key) == null) {
                list.add("$." + path);
                continue;
            }
            Class<?> type = jsonObject.get(key).getClass();
//            System.out.println(">>>type: "+type.toString() +", path: "+path);

            switch (type.toString()) {
                case "class com.alibaba.fastjson.JSONArray":   // 处理json数组
                    JSONArray jsonArray = jsonObject.getJSONArray(key);
                    if (jsonArray.size() < 1) {  //处理空json数组
                        list.add("$." + path);
                    } else {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            String tmp_type = jsonArray.get(i).getClass().toString();
                            //数组下是JSONObject
                            if (jsonArray.get(i).getClass().toString().equals("class com.alibaba.fastjson.JSONObject")) {
                                JSONObject object_temp = jsonArray.getJSONObject(i);
                                parserKeyPath(object_temp, path + "[" + i + "]", list);
                            } else {//数组下非JSONObject
                                list.add("$." + path + "[" + i + "]");
                            }
                        }
                    }
                    break;
                case "class com.alibaba.fastjson.JSONObject":      // 处理json
                    JSONObject object_temp = jsonObject.getJSONObject(key);
                    if(object_temp.size()==0){
                        list.add("$." + path);
                    } else {
                        parserKeyPath(object_temp, path, list);
                    }
                    break;
                default:                                   // 处理其他类型，如int、string
                    list.add("$." + path);
            }
        }
        return list;
    }

    public static List<String> parserKeyPath(JSONObject jsonObject) {
        List<String> list = new ArrayList<>();
        return parserKeyPath(jsonObject, "", list);
    }


    /**
     * 检查json中每个keyPath 下的value 是否符合规则中的正则
     *
     * @param data     待检查的json对象
     * @param rule     规则json，key为待检查对象的keyPath(其中，数组下表用*)，value为正则, 特别注意：此处的rule keyPath深度为1
     * @param errorMap 存储错误信息
     * @return ：
     */
    public static boolean checkJsonByRegular(JSONObject data, JSONObject rule, JSONObject errorMap) {
        List<String> path_list_data = parserKeyPath(data);
        // 如果规则为空，直接返回true
        if (rule == null) {
            return true;
        }
        if (data == null) {
            return false;
        }

        //规则的所有key，每当匹配一次，即删除对应的key，最后剩余的key中再排除掉匹配过的数组，即为data中没有的值
        //特别注意，次数不能直接使用 rule.keySet()， 需要先clone生成一个局部变量，否则会公用变量
        JSONObject jsonObject = (JSONObject) rule.clone();
        Set<String> rule_KeySetCopy = jsonObject.keySet();

        //Check1：先以data中的key为依据，每当匹配一次，即删除rule中对应的key
        for (String path : path_list_data) {
            try {
                String tmp_data = String.valueOf(JSONPath.eval(data, path));

                //规则json中key为待检查对象的keyPath(其中，数组下表用*)
                String path_rule = path;
                if (!rule.keySet().contains(path_rule)) {
                    //当不存在具体key时
                    path_rule = path_rule.replaceAll("\\[\\d+]", "[*]");
                }
                String tmp_rule = rule.getString(path_rule);
                if (tmp_rule == null) {  //如果未配置此keyPath下的规则，则结束本次循环，继续check
                    //TODO: rule 中设置父级路径，而没有具体的路径时，需要增加判断
                    for (String pathRuleTmp : rule.keySet()) {
                        if (path_rule.startsWith(pathRuleTmp)) {
                            path_rule = pathRuleTmp;  //此处设置path_rule 为匹配的父级路径，为下面的 remove_rule_path 使用
                            tmp_rule = rule.getString(pathRuleTmp);
                            break;
                        }
                    }
                    if (tmp_rule == null) { // 遍历技术后无父级路径，则继续检查下个对象
                        continue;
                    }
                }

                //删除已经匹配过的规则，剩余的规则都是未匹配的，即data中无此path规则下的内容，此种情况下的匹配结果为：false
                remove_rule_path(path_rule, rule_KeySetCopy);

                Pattern p = Pattern.compile(tmp_rule);
                Matcher m = p.matcher(tmp_data);
                boolean tmp_res = m.find();

                if (!tmp_res) {
                    logger.info(" >>>>>> 不匹配的 JsonPath： " + path);
                    logger.info(" >>>>>> 此path下的预期结果格式是： " + tmp_rule);
                    logger.info(" >>>>>> 此path下的实际结果内容是： " + tmp_data);
                    errorMap.put(path, String.format("实际值：<%s>，预期值：<%s>", tmp_data, tmp_rule));
                }
            } catch (PatternSyntaxException e){
                //捕获语法错误的异常，遍历拿到语法错误的key，放进errorMap
                logger.error(e.toString());
                try{
                    String pattern = e.getPattern();
                    Set<String> keys = rule.keySet();
                    for (String key:keys){
                        if (rule.getString(key).equals(pattern)){
                            String errorMsg = "请修改\'" + key + "\':\'" + pattern + "\'的语法问题";
                            errorMap.put("语法错误", errorMsg);
                        }
                    }
                }catch (Exception error){
                    logger.error(error.toString());
                    errorMap.put("fault", e.toString());
                }
            }
            catch (Exception e) {
                logger.error(e.toString());
                return false;
            }
        }


        //Check2：解决规则rule中有的key但在data中不存在的情况，此时rule中的key应该是个明确的路径，即不包含[*]
        //TODO：此处可能有问题，也有可能包含[*]
        for (String key : rule_KeySetCopy) {
            String tmp_rule = rule.getString(key);
            logger.info(" >>>>>> 不匹配的 JsonPath： " + key);
            logger.info(" >>>>>> 此path下的预期结果格式是： " + tmp_rule);
            logger.info(" >>>>>> 此path下的实际结果内容是：缺少key ");
            errorMap.put(key, String.format("实际值：<缺失> ，预期值：<%s>", tmp_rule));
        }

        //根据errorMap中是否有值判断是否有错
        return errorMap.size() <= 0;
    }

    public static JSONObject checkJsonByRegular(JSONObject data, JSONObject rule) {
        JSONObject errorMap = new JSONObject();
        boolean result = checkJsonByRegular(data, rule, errorMap);
        JSONObject rsp = new JSONObject();
        rsp.put("result", result);
        rsp.put("detail", errorMap);
        rsp.put("data", data);
        rsp.put("rule", rule);
        return rsp;
    }


    /**
     * 根据简单规则文件和附加的数据，检查json中每个keyPath 是否符合正则或者和附加数据一致.
     * 支持data为多相同格式的数组，rule为单数组。
     * data 中的数据可以有多余的keyPath（即rule中可以缺少部分keyPath）， 但rule中所有的keyPath对应的data数据必须匹配
     * 如何兼容 data 中的 {} [] ， 如 $.data = [1,2] 或者 $.data = [] 都认为通过
     *
     * @param data   待检查的数据
     * @param rule   简单规则文件，即其中的结构和data结构基本一致。
     *               支持data为多相同格式的数组，rule为单数组。
     *               检查的keyPath是以 data 中为主：
     *               当 data 的 某个keyPath 在 rule 中无对应的规则时，返回true（对应场景：data冗余）；
     *               当 rule 中 有多余的keyPath，即data中无此keyPath，返回false（对应场景：data字段缺失）
     * @param expect 附加的预期数据，可能只包含部分的预期数据
     * @param isAllowNull 是否允许 data中父级path为空(仅限[], {})，即：兼容 data 中的 {} [] ， 如 $.data = [1,2] 或者 $.data = [] 都认为通过
     * @return rsp
     */
    public static JSONObject checkJsonBySimpleRegularAndAttachData(JSONObject data, JSONObject rule, JSONObject expect, boolean isAllowNull) {
        JSONObject errorMap = new JSONObject();
        boolean result = true;

        List<String> path_list_data = parserKeyPath(data);
        // 如果规则为空，直接返回true； 数据为空，直接返回false
        if (rule == null || data == null) {
            JSONObject rsp = new JSONObject();
            rsp.put("result", rule == null);
            rsp.put("detail", errorMap);
            rsp.put("data", data);
            rsp.put("rule", rule);
            return rsp;
        }

        //规则的所有key，每当匹配一次，即删除对应的key，最后剩余的key中再排除掉匹配过的数组，即为data中没有的值
        //特别注意，此处不能直接使用 rule.keySet()， 需要先clone生成一个局部变量，否则会公用变量
        JSONObject ruleClone = (JSONObject) rule.clone();
        List<String> rule_keyPathCopy = parserKeyPath(ruleClone);

        for (String path : path_list_data) {
            String actualData = String.valueOf(JSONPath.eval(data, path));

            //兼容 data 中的 {} [] ， 如 $.data = [1,2] 或者 $.data = [] 都认为通过
            if(isAllowNull && (actualData.equals("[]") || actualData.equals("{}"))){
                rule_keyPathCopy.removeIf(s -> s.startsWith(path));
                continue;
            }

            //首先查询是否存在具体路径下的规则
            Object objRulePath = JSONPath.eval(rule, path);
            //针对JSON数组，支持data为多相同格式的数组，rule为单数组即可；非相同格式的数组，需要详细定义rule，前一语句已经兼容此case
            objRulePath = objRulePath == null ? JSONPath.eval(rule, path.replaceAll("\\[\\d+]", "[0]")) : objRulePath;

//            if (objRulePath == null) {
//                objRulePath = JSONPath.eval(rule, path.replaceAll("\\[\\d+]", "[0]"));
//            }

            if (objRulePath != null) {

                // 删除rule clone对象的对应path，当遍历data后，rule clone 中仍有值，则表明部分rule中的keyPath在data中无值
                rule_keyPathCopy.remove(path);

                // 获取实际值中每个相对应的keyPath在rule中的值，分三种场景：
                // 1. 对应except中的具体值，形如： {{$.tid}}
                // 2. 为正则表达式，以 ^ 开头，$ 结尾
                // 3. 为其他精确匹配值，如 0 ， "0" 等
                String ruleValue = String.valueOf(JSONPath.eval(rule, path)); // 对应的path 在 rule 可能不存在
                if (ruleValue.startsWith("{{") && ruleValue.endsWith("}}")) {
                    String expectDataPath = ruleValue.substring(2, ruleValue.length() - 2);
                    String expectData = String.valueOf(JSONPath.eval(expect, expectDataPath));
                    if (!actualData.equals(expectData)) {
                        result = false;
                        errorMap.put(path, String.format("实际值：[%s] ，预期值：[%s]", actualData, expectData));
                    }
                } else if (ruleValue.startsWith("^")) {  // rule 中是正则匹配模式
                    Pattern p = Pattern.compile(ruleValue);
                    Matcher m = p.matcher(actualData);
                    boolean tmp_res = m.find();

                    if (!tmp_res) {
                        result = false;
                        errorMap.put(path, String.format("实际值：[%s]，预期值：[%s]", actualData, ruleValue));
                    }
                } else { // rule 中是精确值匹配
                    if (!actualData.equals(ruleValue)) {
                        result = false;
                        errorMap.put(path, String.format("实际值：[%s]，预期值：[%s]", actualData, ruleValue));
                    }
                }
            }
        }

        for (String key : rule_keyPathCopy) {
            result = false;
            String tmp_rule = rule.getString(key);
            errorMap.put(key, String.format("实际值：[缺失] ，预期值：[%s]", tmp_rule));
        }

        JSONObject rsp = new JSONObject();
        rsp.put("result", result);
        rsp.put("detail", errorMap);
        rsp.put("data", data);
        rsp.put("rule", rule);
        return rsp;
    }

    public static JSONObject checkJsonBySimpleRegularAndAttachData(JSONObject data, JSONObject rule, JSONObject expect){
        return checkJsonBySimpleRegularAndAttachData(data, rule, expect, true);
    }

    public static JSONObject checkJsonBySimpleRegular(JSONObject data, JSONObject rule){
        return checkJsonBySimpleRegularAndAttachData(data, rule, null, true);
    }
    /**
     * 删除所有下属子级和最近一层父级。
     * 删除子级规则， 如 当前JsonPath为：$.data.consumption[*].account_id，
     * 子级可以是 $.data.consumption[*].account_id[*].id    $.data.consumption[*].account_id[*].name 等；
     * 父级是：$.data.consumption
     *
     * @param path ： 父级JsonPath
     * @param list ： 所有规则list
     *             <p>
     *             list 循环删除时，不能使用list.remove，否则会报并发修改错误： java.util.ConcurrentModificationException
     *             应该使用Iterator , 或者 Collection.removeIf
     */
    public static void remove_rule_path(String path, Set<String> list) {

        //删除当前JsonPath
        list.remove(path);

        //删除最近一层父级JsonPath
        String path_p;
        if (path.contains("[")) {
            path_p = path.substring(0, path.lastIndexOf("["));
            if (list.contains(path_p)) {
                list.remove(path_p);
            }
        }

        //删除所有子级JsonPath
//            list.removeIf(path_tmp -> path_tmp.startsWith("$." + path + "[*]."));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String path_tmp = it.next();
            if (path_tmp.startsWith(path + "[")) {
                it.remove();
            }
        }
    }

//    public static void main(String[] args) {
//        String data = "{\n" +
//                "  \"return_message\": \"\",\n" +
//                "  \"task_info\": {\n" +
//                "    \"error_message\": \"\",\n" +
//                "    \"progress_message\": \"接收到任务\",\n" +
//                "    \"open_id\": \"z1etzi00dbpzc7b7dzazdjlulznfjz25mxrpges9\",\n" +
//                "    \"organization_id\": \"1030226\",\n" +
//                "    \"next_action\": \"CONTINUE\",\n" +
//                "    \"organization_type\": \"TELECOM\",\n" +
//                "    \"progress\": \"0.1\",\n" +
//                "    \"error_code\": \"0\",\n" +
//                "    \"task_id\": \"467hm9ci317f8\",\n" +
//                "    \"status\": \"ACCEPT\"\n" +
//                "  },\n" +
//                "  \"return_code\": \"0\"\n" +
//                "}";
//
//        String rule = "{\n" +
//                "  \"return_message\": \"\",\n" +
//                "  \"task_info\": {\n" +
//                "    \"error_message\": \"\",\n" +
//                "    \"progress_message\": \"接收到任务\",\n" +
//                "    \"open_id\": \"{{$.openID}}\",\n" +
//                "    \"organization_id\": \"{{$.orgID}}\",\n" +
//                "    \"next_action\": \"CONTINUE\",\n" +
//                "    \"organization_type\": \"{{$.orgType}}\",\n" +
//                "    \"progress\": \"0.1\",\n" +
//                "    \"error_code\": \"0\",\n" +
//                "    \"task_id\": \"{{$.tid}}\",\n" +
//                "    \"status\": \"ACCEPT\"\n" +
//                "  },\n" +
//                "  \"return_code\": \"0\"\n" +
//                "}";
//
//        String expect = "{\n" +
//                "    \"env\":\"测试环境\",\n" +
//                "    \"token\":\"7021c1c4682b98fa586edde341276dee5a57c7deca6d4e8731d0f86c5b18b00d\",\n" +
//                "    \"tid\":\"77r7h7qiikn9a\",\n" +
//                "    \"openID\":\"z1etzi00dbpzc7b7dzazdjlulznfjz25mxrpges9\",\n" +
//                "    \"orgType\":\"COMMERCE\"\n" +
//                "}";
//
//        System.out.println(JsonUtil.checkJsonBySimpleRegularAndAttachData(JSONObject.parseObject(data), JSONObject.parseObject(rule),
//                JSONObject.parseObject(expect)));
//
//    }


}
