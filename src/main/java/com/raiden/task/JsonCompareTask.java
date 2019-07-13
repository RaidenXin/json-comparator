package com.raiden.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.raiden.util.JsonUtils;
import com.raiden.util.StringUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 20:05 2019/7/13
 * @Modified By: json对比类
 */
public class JsonCompareTask extends AbstractTask{

    private JTextPane left;
    private JTextPane right;
    private int type;

    public JsonCompareTask(int type,JTextPane... jTextPanes){
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
        Object leftObject = preconditioning(leftJson);
        Object rightObject = preconditioning(rightJson);
        leftJson = JsonUtils.responseFormat(JSON.toJSONString(leftObject, SerializerFeature.SortField));
        rightJson = JsonUtils.responseFormat(JSON.toJSONString(rightObject, SerializerFeature.SortField));
        String[] leftJsons = leftJson.split("\n");
        String[] rightJsons = rightJson.split("\n");
        try {
            setColor(left);
            setColor(right);
            for (int i = 0, n = Math.max(leftJsons.length, rightJsons.length); i < n; i++){
                Document leftDocument = left.getDocument();//获得文本对象
                Document rightDocument = right.getDocument();//获得文本对象
                if (i >= leftJsons.length){
                    String rightValue = rightJsons[i];
                    rightDocument.insertString(rightDocument.getLength(), "\n" + rightValue, left.getStyle("red"));
                }else if (i >= rightJsons.length){
                    String leftValue = leftJsons[i];
                    leftDocument.insertString(leftDocument.getLength(), "\n" + leftValue, left.getStyle("red"));
                }else {
                    String leftValue = leftJsons[i];
                    String rightValue = rightJsons[i];
                    if (compare(leftValue, rightValue)){
                        leftDocument.insertString(leftDocument.getLength(), "\n" + leftValue, left.getStyle("normal"));
                        rightDocument.insertString(rightDocument.getLength(), "\n" + rightValue, left.getStyle("normal"));
                    }else {
                        leftDocument.insertString(leftDocument.getLength(), "\n" + leftValue, left.getStyle("red"));
                        rightDocument.insertString(rightDocument.getLength(), "\n" + rightValue, left.getStyle("red"));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setColor(JTextPane textPane){
        Style def = textPane.getStyledDocument().addStyle(null, null);
        StyleConstants.setFontFamily(def, "verdana");
        StyleConstants.setFontSize(def, 12);
        Style normal = textPane.addStyle("normal", def);
        Style s = textPane.addStyle("red", normal);
        StyleConstants.setForeground(s, Color.RED);
        textPane.setParagraphAttributes(normal, true);
    }

    private boolean compare(String leftValue,String rightValue){
        if (type == 1){
            return leftValue.equals(rightValue);
        }
        String[] leftSplit = leftValue.split(":");
        String[] rightSplit = rightValue.split(":");
        return leftSplit[0].equals(rightSplit[0]);
    }
}
