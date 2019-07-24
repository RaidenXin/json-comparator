package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.raiden.util.JsonUtils;
import com.raiden.util.StringUtil;
import com.raiden.viwe.TextAreaFrame;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.text.BadLocationException;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 12:00 2019/7/13
 *  *  * @Modified By: json排序策略类
 */
@Setter
@Getter
public class JsonParseTask extends AbstractTask{

    private JTextPane left;
    private JTextPane right;

    public JsonParseTask(JTextPane... jTextPanes) {
        this.left = jTextPanes[0];
        this.right = jTextPanes[1];
    }

    @Override
    public void execute() throws Exception{
        String leftJson = sort(left);
        String rightJson = sort(right);
        if (leftJson.length() > rightJson.length()){
            rightJson = makeUpLenght(leftJson, rightJson);
        }else {
            leftJson = makeUpLenght(rightJson, leftJson);
        }
        setText(left, leftJson);
        setText(right, rightJson);
    }

    private String sort(JTextPane jTextPane){
        String text = jTextPane.getText();
        if (StringUtil.isBlank(text) || TextAreaFrame.CONTENT_TEXT.equals(text)){
            return text;
        }
        Object object = preconditioning(jTextPane, jTextPane.getText());
        if (null == object){
            return jTextPane.getText();
        }
        String result = JsonUtils.responseFormat(JSON.toJSONString(object, SerializerFeature.SortField));
        return result;
    }

    private String makeUpLenght(String source,String target){
        if (source.length() < target.length()){
            return target;
        }
        StringBuilder builder = new StringBuilder(target);
        for (int i = target.length(); i < source.length(); i++){
            builder.append("\n");
        }
        return builder.toString();
    }


    private void setText(JTextPane jTextPane,String text) throws BadLocationException {
        setColor(jTextPane);
        jTextPane.setText(text);
    }
}
