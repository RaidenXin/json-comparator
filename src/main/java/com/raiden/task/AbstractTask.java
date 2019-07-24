package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.raiden.logs.Logger;
import com.raiden.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 20:19 2019/7/13
 * @Modified By: 抽象任务类
 */
public abstract class AbstractTask implements Task{

    private Logger logger = Logger.newInstance();

    protected Object preconditioning(JTextPane jTextPane, String json){
        //干掉第一个大括号之前和最后一个大括号之后的字符
        try {
            if (null != json && json.indexOf("{") > -1 && json.indexOf("}") > -1){
                json = StringUtil.trim(json, "{", "}");
            }
            //看看是否被转译过，去掉转译
            json = StringUtils.replace(json, "\\\"", "\"");
            JSONObject object = JSON.parseObject(json);
            json = JSON.toJSONString(object, SerializerFeature.SortField);
        }catch (Throwable e){
            logger.error(e);
            e.printStackTrace();
            jTextPane.setText("请检查你的JSON串是否正确！");
            return null;
        }
        Map<String, Object> treeMap = sortField(JSON.parseObject(json, Map.class));
        return treeMap;
    }


    private Map<String, Object> sortField(Map<String, Object> map){
        //new 一个排序的Map 在构造方法中传入比较器
        Map<String, Object> treeMap = new TreeMap<>(new MapComparator());
        for (Map.Entry<String, Object> entry : map.entrySet()){
            Object value = entry.getValue();
            //去掉空和空字符串
            if (value == null || (value instanceof String && StringUtil.isBlank((String) value))){
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

    /**
     * 设置颜色模板
     * @param textPane
     */
    protected void setColor(JTextPane textPane){
        Style def = textPane.getStyledDocument().addStyle(null, null);
        StyleConstants.setFontFamily(def, "verdana");
        StyleConstants.setFontSize(def, 12);
        Style normal = textPane.addStyle("normal", def);
        Style s = textPane.addStyle("red", normal);
        StyleConstants.setForeground(s, Color.RED);
        textPane.setParagraphAttributes(normal, true);
    }
}
