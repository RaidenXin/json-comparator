package com.raiden.task;

import com.raiden.util.StringUtil;
import com.raiden.viwe.TextAreaFrame;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetFieldValueTask extends AbstractTask{

    private static final String LINE_BREAK = "\n";
    private static final Pattern PATTERN = Pattern.compile("[var|(\\w+)] (\\w+) = new (\\w+)\\(\\)");
    private static final String SET = ".set";
    private static final String GET = ".get";
    private static final String[] BE_EQUAL_TO = {" = ", " =", "= ", "="};

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
        Matcher matcher = PATTERN.matcher(start);
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
                }else if (rowText.startsWith("//")){
                    builder.append(LINE_BREAK);
                    builder.append(rowText);
                    continue;
                }
                int index = rowText.indexOf("//");
                String note = StringUtils.EMPTY;
                if (index > -1 ){
                    note = rowText.substring(index);
                    rowText =  rowText.substring(0, index);
                }
                int lastSpotIndex = -1;
                if (rowText.indexOf("=") < (lastSpotIndex = rowText.lastIndexOf("."))){
                    rowText = rowText.substring(0, lastSpotIndex) + GET + StringUtil.firstLetterCapitalized(rowText.substring(lastSpotIndex + 1)) + "()";
                }
                rowText = StringUtils.replaceEach(StringUtil.firstLetterCapitalized(rowText), BE_EQUAL_TO, new String[]{"(","(","(","("});
                builder.append(LINE_BREAK);
                builder.append(variableName);
                builder.append(SET);
                builder.append(rowText);
                builder.append(");");
                builder.append(note);
            }
            right.setText(builder.toString());
        }
    }
}
