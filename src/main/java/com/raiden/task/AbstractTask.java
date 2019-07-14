package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.raiden.util.StringUtils;

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
        Object jsonObject = JSON.parseObject(json);
        return jsonObject;
    }
}
