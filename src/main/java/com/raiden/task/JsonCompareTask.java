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
import java.util.Stack;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 20:05 2019/7/13
 * @Modified By: json对比策略类
 */
public class JsonCompareTask extends AbstractTask{

    private Logger logger = Logger.newInstance();
    private static final int MAX = 1000000;

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
        Stack<String> stack = new Stack<>();
        try {
            String[] leftJsons = getJsonArry(left, leftJson);
            String[] rightJsons = getJsonArry(right, rightJson);
            int leftIndex, rightIndex, sum;
            leftIndex = rightIndex = sum = 0;
            while (sum < MAX){
                Document leftDocument = left.getDocument();//获得文本对象
                Document rightDocument = right.getDocument();//获得文本对象
                //将右边多余的全部输出
                if (leftIndex >= leftJsons.length && rightIndex < rightJsons.length){
                    rightDocument.insertString(rightDocument.getLength(), "\n" + rightJsons[rightIndex++], left.getStyle("red"));
                }else if (rightIndex >= rightJsons.length && leftIndex < leftJsons.length){
                    //将左边多余的全部输出
                    leftDocument.insertString(leftDocument.getLength(), "\n" + leftJsons[leftIndex++], left.getStyle("red"));
                }else if (rightIndex < rightJsons.length && leftIndex < leftJsons.length){
                    String leftValue = leftJsons[leftIndex];
                    StringBuilder leftBuilder = new StringBuilder();
                    //循环右边的 元素与左边已经取出的元素进行对比 直到对比到相等的或者这个json块的终止符（最外层的右半边大括号）
                    for (int i = rightIndex; i < rightJsons.length; i++){
                        String rightValue = rightJsons[i];
                        //如果遇到是个左半边大括号 就放入栈中
                        if ("{".equals(rightValue) && i != rightIndex){
                            stack.push(rightValue);
                        }
                        //遇到终结符 且栈不为空就弹栈
                        if ("}".equals(rightValue) && !stack.empty()){
                            stack.pop();
                            //这里也还在其他代码块中 所以也不用比较
                            continue;
                        }
                        //如果栈不为空 则说明进入到其他json块中 不用比较
                        if (!stack.empty()){
                            continue;
                        }
                        //如果能到这 说明回到了原来代码块中 可以继续比较
                        //如果遇到了相同的 以原色输出到界面 并且将左边leftBuilder 累加的换回符也一并输出 全部下标+1
                        if (compare(leftValue, rightValue)) {
                            //将左边累加的换行一并输出
                            leftBuilder.append("\n" + leftValue);
                            leftDocument.insertString(leftDocument.getLength(), leftBuilder.toString(), left.getStyle("normal"));
                            leftIndex++;
                            //比较是不是第一次比就遇到了正确的 如果是就输出 右边下标+1
                            if (rightIndex == i){
                                rightDocument.insertString(rightDocument.getLength(), "\n" + rightValue, left.getStyle("normal"));
                                rightIndex++;
                            }else {
                                //如果不是 就将右边 rightIndex 位置的字符 到 i - 1 位置的字符全部以红色输出 并且让 rightIndex = i+1
                                for (int j = rightIndex; j < i;j++){
                                    rightDocument.insertString(rightDocument.getLength(), "\n" + rightJsons[j], left.getStyle("red"));
                                }
                                rightDocument.insertString(rightDocument.getLength(), "\n" + rightValue, left.getStyle("normal"));
                                rightIndex = i + 1;
                            }
                            break;
                        }else {
                            //如果不相同 就一直比 直到遇到相同的 或者遇到 右半边大括号
                            //不相同 左边新增一个换行符
                            leftBuilder.append("\n");
                            //判断是不是终结符
                            if ("}".equals(rightValue)){
                                // 就终止对比 将左边以红色输出 给右边输出加一个空行符
                                leftDocument.insertString(leftDocument.getLength(), "\n" + leftJsons[leftIndex++], left.getStyle("red"));
                                rightDocument.insertString(rightDocument.getLength(), "\n", left.getStyle("red"));
                                //清空左边累加的换行符
                                leftBuilder.delete(0, leftBuilder.length());
                                break;
                            }
                        }
                    }
                }
                sum++;
            }
        }catch (Exception e){
            logger.error("JsonCompareTask is error", e);
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
        json = JsonUtils.responseFormat(JSON.toJSONString(jsonObject));
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
            if (leftValue.equals(rightValue)){
                return true;
            }
            int i = leftValue.length() - rightValue.length();
            // 如果他们长度仅仅相差一位。 肯能是因为最后多了逗号引起的不相等， 可以去掉最后一位在比较
            if (i == -1){
                rightValue = rightValue.substring(0, rightValue.length() - 1);
            }else if (i == 1){
                leftValue = leftValue.substring(0, leftValue.length() - 1);
            }
            return leftValue.equals(rightValue);
        }
        String[] leftSplit = leftValue.split(":");
        String[] rightSplit = rightValue.split(":");
        return leftSplit[0].equals(rightSplit[0]);
    }
}
