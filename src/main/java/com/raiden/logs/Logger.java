package com.raiden.logs;

import com.raiden.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志类。用的最简单的单例
 */
public final class Logger {

    private static final String pattern = "yyyy-MM-dd HH:mm:ss";
    private static final LogsStack stack = LogsStack.newInstance();
    private static final Logger logger = new Logger();

    private Logger(){
    }

    public final static Logger newInstance(){
        return logger;
    }

    public void info(String log){
        Date time = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        StringBuilder builder = new StringBuilder(simpleDateFormat.format(time));
        builder.append("\t");
        builder.append(log);
        stack.push(builder.toString());
    }

    public void error(String errorStr){
        stack.errorAdd(errorStr);
    }

    public void error(Throwable e){
        error(StringUtil.EMPTY, e);
    }

    public void error(String errorStr, Throwable e){
        stack.errorAdd(print(errorStr, e));
    }

    private String print(String errorStr, Throwable e){
        Date time = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        StringBuilder builder = new StringBuilder(simpleDateFormat.format(time));
        builder.append("\tERROR\t");
        builder.append(errorStr);
        builder.append(e.getClass().getName());
        builder.append("\t:");
        builder.append(e.getMessage());
        builder.append("\r\n");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            time = new Date();
            builder.append(simpleDateFormat.format(time));
            builder.append("\tERROR\tat\t");
            builder.append(stackTraceElement.getClassName());
            builder.append(".");
            builder.append(stackTraceElement.getMethodName());
            builder.append("(");
            builder.append("Line in line");
            builder.append(stackTraceElement.getLineNumber());
            builder.append(")\r\n");
        }
        for (Throwable throwable : e.getSuppressed()){
            builder.append(print("Caused by: ", throwable));
        }
        Throwable cause = e.getCause();
        if (null != cause){
            builder.append(print("Caused by: ", cause));
        }
        return builder.toString();
    }
}
