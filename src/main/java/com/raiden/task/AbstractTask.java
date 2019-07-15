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
        json = StringUtils.trim(json, "{", "}");
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
        Map<String, Object> treeMap = new TreeMap<>(new MapComparator());
        for (Map.Entry<String, Object> entry : map.entrySet()){
            Object value = entry.getValue();
            if (value == null){
                continue;
            }
            if (value instanceof JSONObject){
                JSONObject jsonObject  = (JSONObject) value;
                treeMap.put(entry.getKey(), sortField(JSON.parseObject(jsonObject.toJSONString(), Map.class)));
            }else if (value instanceof JSONArray){
                JSONArray jsonArray = (JSONArray) value;
                List<Object> list = new ArrayList<>(jsonArray.size());
                for (Object object : jsonArray){
                    JSONObject jsonObject  = (JSONObject) object;
                    Map<String, Object> map1 = sortField(JSON.parseObject(jsonObject.toJSONString(), Map.class));
                    list.add(map1);
                }
                treeMap.put(entry.getKey(), list);
            }else if (value instanceof String){
                if (StringUtils.isNotBlank((String) value)){
                    treeMap.put(entry.getKey(), value);
                }
            }else {
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
}
