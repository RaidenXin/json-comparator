package com.raiden.task;

import com.raiden.util.ConfigUtils;
import com.raiden.util.StringUtil;
import com.raiden.viwe.TextAreaFrame;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Properties;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 23:13 2019/8/1
 * @Modified By:
 */
public class NetToJavaTask extends AbstractTask {

    private static final String propertiesName = "NetToJava.properties";
    private static final String LINE_BREAK = "\n";
    private static final String BLANK_DOUBLE = "    ";
    private static final String SEARCH_STRINGS = "searchStrings";
    private static final String REPLACEMENTDS = "replacementds";

    public NetToJavaTask(JTextPane... jTextPanes){
        super(jTextPanes);
    }

    @Override
    public void execute() throws Exception {
        String content = left.getText();
        if (StringUtils.isBlank(content) || content.contains(TextAreaFrame.CONTENT_TEXT)){
            return;
        }
        Properties properties = ConfigUtils.getProperties(propertiesName);
        if (null == properties){
            return;
        }
        int startIndex = content.indexOf("{");
        int endIndex = content.lastIndexOf("}");
        String start = StringUtils.replace(content.substring(0, startIndex), LINE_BREAK, StringUtils.EMPTY);
        String text = content.substring(startIndex + 1, endIndex);
        String end = content.substring(endIndex);
        String searchStrings = properties.getProperty(SEARCH_STRINGS);
        String replacementds = properties.getProperty(REPLACEMENTDS);
        if (StringUtils.isAnyBlank(searchStrings, replacementds)){
            return;
        }
        //1.先将 public 替换成 private
        text = StringUtils.replaceEach(text, StringUtils.split(searchStrings,","), StringUtils.split(replacementds,","));
        if (StringUtils.isBlank(text)){
            return;
        }
        StringBuilder builder = new StringBuilder("import lombok.Setter;\nimport lombok.Getter;\n\n\n@Getter\n@Setter\n");
        builder.append(start.trim());
        builder.append("{");
        String[] textArray = text.split(LINE_BREAK);
        for (String str : textArray){
            if (StringUtils.isBlank(str)){
                continue;
            } else if (str.indexOf("[") > -1 && str.indexOf("]") > -1){
                builder.append(LINE_BREAK);
                builder.append("//");
                builder.append(BLANK_DOUBLE);
                builder.append(str.trim());
            }else if (str.indexOf("(") > -1 && str.indexOf(")") > -1){//过滤方法
                continue;
            }else if (str.indexOf("//") > -1){
                builder.append(LINE_BREAK);
                builder.append(BLANK_DOUBLE);
                builder.append(str.trim());
            }else if (str.indexOf("private") > -1){
                builder.append(LINE_BREAK);
                builder.append(BLANK_DOUBLE);
                builder.append(replaceClassType(properties, str.trim()));
            }
        }
        builder.append("\n");
        builder.append(end);
        right.setText(builder.toString());
    }

    private String replaceClassType(Properties properties, String text){
        text = StringUtils.replace(text, ";", StringUtils.EMPTY).trim() + ";";
        boolean isBool = text.indexOf("bool?") > -1 || text.indexOf("bool") > -1;
        String[] testArray = StringUtils.split(text, " ");
        String result = text;
        if (testArray.length > 2){
            for (int i = 0; i < testArray.length; i++){
                String key = testArray[i];
                String value;
                if (StringUtils.isNotBlank(key) && (value = properties.getProperty(key)) != null){
                    result = StringUtils.replace(result, key, value);
                }else if (i == testArray.length - 1){
                    key = isBool && (key.startsWith("is") || key.startsWith("Is")) ? key.substring(2, key.length()) : key;
                    result = StringUtils.replace(result, key, StringUtil.firstLetterLowercase(key));
                }
            }
        }
        return result;
    }
}
