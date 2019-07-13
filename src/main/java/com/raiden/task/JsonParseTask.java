package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.concurrent.ForkJoinTask;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 12:00 2019/7/13
 * @Modified By:
 */
@Setter
@Getter
public class JsonParseTask{

    private String json;
    private JTextArea jTextArea;

    public JsonParseTask(String json,JTextArea jTextArea) {
        this.json = json;
        this.jTextArea = jTextArea;
    }
}
