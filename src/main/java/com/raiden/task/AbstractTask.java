package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.raiden.util.StringUtils;

import java.util.*;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 20:19 2019/7/13
 * @Modified By: 抽象任务类
 */
public abstract class AbstractTask implements Task{

    protected Object preconditioning(String json){
        //干掉第一个大括号之前和最后一个大括号之后的字符
        if (null != json && json.indexOf("{") > -1 && json.indexOf("}") > -1){
            json = StringUtils.trim(json, "{", "}");
        }
        //看看是否被转译过，去掉转译
        if (json.indexOf("\\") > -1){
            json = json.replaceAll("\\\\", "");
        }
        JSONObject object = JSON.parseObject(json);
        json = JSON.toJSONString(object, SerializerFeature.SortField);
        Map<String, Object> treeMap = sortField(JSON.parseObject(json, Map.class));
        return treeMap;
    }


    private Map<String, Object> sortField(Map<String, Object> map){
        //new 一个排序的Map 在构造方法中传入比较器
        Map<String, Object> treeMap = new TreeMap<>(new MapComparator());
        for (Map.Entry<String, Object> entry : map.entrySet()){
            Object value = entry.getValue();
            //去掉空和空字符串
            if (value == null || (value instanceof String && StringUtils.isBlank((String) value))){
                continue;
            }
            //判断是不是 JSONObject 如果是转化成TreeMap
            if (value instanceof JSONObject){
                //JSONObject 覆写了toString 所有可以直接调用toString
                treeMap.put(entry.getKey(), sortField(JSON.parseObject(value.toString(), Map.class)));
                //判断是不是 JSONArray
            }else if (value instanceof JSONArray){
                //如果是 强转成 JSONArray 并且遍历它
                JSONArray jsonArray = (JSONArray) value;
                List<Object> list = new ArrayList<>(jsonArray.size());
                for (Object object : jsonArray){
                    //判断里面的是否是 JSONObject 如果是转化成TreeMap
                    if (object instanceof JSONObject){
                        object = sortField(JSON.parseObject(object.toString(), Map.class));
                    }
                    //其他基础类型直接放进去
                    list.add(object);
                }
                treeMap.put(entry.getKey(), list);
            }else {
                //其他基础类型直接放进去
                treeMap.put(entry.getKey(), value);
            }
        }
        return treeMap;
    }


    class MapComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    class ListComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            //因为这里面不是基础类型就是Map 或者他们的 map的toString方法被复写过 会把所有的key和value组合拼接成字符串
            //比如 map.put("key":"value"); toString 会变成 {"key":"value"}
            String str1 = o1.toString();
            String str2 = o2.toString();
            //获得其字符串长度
            Integer length1= str1.length();
            Integer length2 = str2.length();
            //先比较其长度
            int i = length1.compareTo(length2);
            //如果长度相同 在比较实际字符串
            if (i == 0){
                return str1.compareTo(str2);
            }
            return i;
        }
    }
}
