package com.raiden.logs;

import com.raiden.util.PathUtils;
import com.raiden.util.StringUtil;

import java.io.File;
import java.io.FileWriter;

/**
 * 日志处理类
 */
public class LogHandler {

    private static final String saveFolderName = "logs\\";
    private static final String Log_File_Name = "Main.log";
    private static final String Error_Log_File_Name = "ErrorMain.log";
    private static final LogsStack stack = LogsStack.newInstance();
    private static String savePath;
    private static final LogHandler logHandler = new LogHandler();

    static {
        String path = LogHandler.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (path.indexOf("/") > -1 && !path.endsWith("/")){
            savePath = path.substring(0, path.lastIndexOf("/") + 1);
        }else if (path.indexOf("\\") > -1 && !path.endsWith("\\")){
            savePath = path.substring(0, path.lastIndexOf("\\") + 1);
        }else {
            savePath = path;
        }
    }

    private LogHandler(){
    }

    public static final LogHandler newInstance(){
        return logHandler;
    }

    public void start(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.err.println("日志线程已经启动！");
                System.err.println(savePath + saveFolderName);
                File file = new File(savePath + saveFolderName);
                if (!file.exists()) {
                    file.mkdir();
                }
                String logFileName = savePath + saveFolderName + Log_File_Name;
                String errorLogFileName = savePath + saveFolderName + Error_Log_File_Name;
                File logs = new File(logFileName);
                File errorLogs = new File(errorLogFileName);
                while (true){
                    if (!stack.logsIsEmpty()){
                        String log = stack.poll();
                        if (StringUtil.isNotBlank(log)){
                            write(logs, logFileName, log);
                        }
                    }
                    if (!stack.errorLogsIsEmpty()){
                        String log = stack.errorPop();
                        if (StringUtil.isNotBlank(log)){
                            write(errorLogs, errorLogFileName, log);
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private void write(File logFile,String fileName,String logs){
        if (!logFile.exists()){
            logFile = new File(fileName);
        }
        try (FileWriter writer = new FileWriter(logFile,true)){
            writer.append(logs);
        }catch (Exception e){
            stack.errorAdd(e.getMessage());
        }
    }
}
