package com.raiden.task;

import com.raiden.base.Info;
import com.raiden.util.StringUtil;
import com.raiden.viwe.TextAreaFrame;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetFieldValueTask extends AbstractTask{

    private static final String LINE_BREAK = "\n";
    private static final Pattern pattern = Pattern.compile("[var|(\\w+)] (\\w+) = new (\\w+)\\(\\)");
    private static final String SET = ".set";

    public SetFieldValueTask(JTextPane... jTextPanes){
        super(jTextPanes);
    }
    @Override
    public void execute() throws Exception {
        String content = left.getText();
        if (StringUtils.isBlank(content) || content.contains(TextAreaFrame.CONTENT_TEXT)){
            return;
        }
        int startIndex = content.indexOf("{");
        int endIndex = content.lastIndexOf("}");
        String start = content.substring(0, startIndex).trim();
        Matcher matcher = pattern.matcher(start);
        if (matcher.find()){
            String variableName = matcher.group(1);
            String className = matcher.group(2);
            String text = content.substring(startIndex + 1, endIndex);
            String[] textArray = StringUtils.split(text, "\n");
            StringBuilder builder = new StringBuilder(StringUtils.replace(start, "var", className));
            builder.append(";");
            for (String row : textArray){
                String rowText = StringUtils.replace(row.trim(), ",", StringUtils.EMPTY);
                if (StringUtils.isBlank(rowText)){
                    continue;
                }
                rowText = StringUtils.replace(StringUtil.firstLetterCapitalized(rowText), " = ", "(");
                builder.append(LINE_BREAK);
                builder.append(variableName);
                builder.append(SET);
                builder.append(rowText);
                builder.append(");");
            }
            right.setText(builder.toString());
        }
    }
}
