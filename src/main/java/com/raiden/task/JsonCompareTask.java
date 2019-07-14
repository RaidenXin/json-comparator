package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.raiden.base.Strategy;
import com.raiden.logs.Logger;
import com.raiden.util.JsonUtils;
import com.raiden.util.StringUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 20:05 2019/7/13
 * @Modified By: json对比策略类
 */
public class JsonCompareTask extends AbstractTask{

    private JTextPane left;
    private JTextPane right;
    private Strategy type;

    public JsonCompareTask(Strategy type,JTextPane... jTextPanes){
        this.type = type;
        this.left = jTextPanes[0];
        this.right = jTextPanes[1];
    }

    @Override
    public void execute() {
        String leftJson = left.getText();
        String rightJson = right.getText();
        left.setText("");
        right.setText("");
        if (StringUtils.isBlank(leftJson) || StringUtils.isBlank(rightJson)){
            return;
        }
        try {
            String[] leftJsons = getJsonArry(left, leftJson);
            String[] rightJsons = getJsonArry(right, rightJson);
            for (int i = 0, n = Math.max(leftJsons.length, rightJsons.length); i < n; i++){
                Document leftDocument = left.getDocument();//获得文本对象
                Document rightDocument = right.getDocument();//获得文本对象
                if (i >= leftJsons.length){
                    rightDocument.insertString(rightDocument.getLength(), "\n" + rightJsons[i], left.getStyle("red"));
                }else if (i >= rightJsons.length){
                    leftDocument.insertString(leftDocument.getLength(), "\n" + leftJsons[i], left.getStyle("red"));
                }else {
                    String leftValue = leftJsons[i];
                    String rightValue = rightJsons[i];
                    int leftLength = i == 0 ? 0 : leftDocument.getLength();
                    int rightLength = i == 0 ? 0 : rightDocument.getLength();
                    if (compare(leftValue, rightValue)){
                        leftDocument.insertString(leftLength, "\n" + leftValue, left.getStyle("normal"));
                        rightDocument.insertString(rightLength, "\n" + rightValue, left.getStyle("normal"));
                    }else {
                        leftDocument.insertString(leftLength, "\n" + leftValue, left.getStyle("red"));
                        rightDocument.insertString(rightLength, "\n" + rightValue, left.getStyle("red"));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取切割好的json数组
     * @param jTextPane
     * @param json
     * @return
     */
    private String[] getJsonArry(JTextPane jTextPane,String json){
        Object jsonObject = preconditioning(json);
        json = JsonUtils.responseFormat(JSON.toJSONString(jsonObject, SerializerFeature.SortField));
        String[] jsonArray = json.split("\n");
        setColor(jTextPane);
        return jsonArray;
    }

    /**
     * 设置颜色模板
     * @param textPane
     */
    private void setColor(JTextPane textPane){
        Style def = textPane.getStyledDocument().addStyle(null, null);
        StyleConstants.setFontFamily(def, "verdana");
        StyleConstants.setFontSize(def, 12);
        Style normal = textPane.addStyle("normal", def);
        Style s = textPane.addStyle("red", normal);
        StyleConstants.setForeground(s, Color.RED);
        textPane.setParagraphAttributes(normal, true);
    }

    /**
     * 不同的对比方法
     * @param leftValue
     * @param rightValue
     * @return
     */
    private boolean compare(String leftValue,String rightValue){
        if (type == Strategy.COMPARE){
            return leftValue.equals(rightValue);
        }
        String[] leftSplit = leftValue.split(":");
        String[] rightSplit = rightValue.split(":");
        return leftSplit[0].equals(rightSplit[0]);
    }
}
