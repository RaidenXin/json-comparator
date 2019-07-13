package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.raiden.util.JsonUtils;
import com.raiden.util.StringUtils;
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
public class JsonParseTask extends AbstractTask{

    private String json;
    private JTextPane jTextPane;

    public JsonParseTask(String json,JTextPane jTextPane) {
        this.json = json;
        this.jTextPane = jTextPane;
    }

    @Override
    public void execute() {
        if (StringUtils.isBlank(json)){
            return;
        }
        Object jsonObject = preconditioning(json);
        String result = JsonUtils.responseFormat(JSON.toJSONString(jsonObject, SerializerFeature.SortField));
        jTextPane.setText(result);
    }
}
