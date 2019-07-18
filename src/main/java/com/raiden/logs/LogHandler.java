package com.raiden.logs;

import com.raiden.util.StringUtils;

import java.io.File;
import java.io.FileWriter;

/**
 * 日志处理类
 */
public class LogHandler {

    private static final String saveFolderName = "\\logs\\";
    private static final String Log_File_Name = "Main.log";
    private static final String Error_Log_File_Name = "ErrorMain.log";
    private static final LogsStack stack = LogsStack.newInstance();
    private static String savePath = LogHandler.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    private static final LogHandler logHandler = new LogHandler();

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
                if (savePath.indexOf("/") > -1){
                    savePath = savePath.substring(0, savePath.lastIndexOf("/"));
                }else {
                    savePath = savePath.substring(0, savePath.lastIndexOf("\\"));
                }
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
                    if (!logs.exists()){
                        logs = new File(savePath + saveFolderName + Log_File_Name);
                    }
                    if (!errorLogs.exists()){
                        errorLogs = new File(savePath + saveFolderName + Error_Log_File_Name);
                    }
                    if (!stack.logsIsEmpty()){
                        String log = stack.poll();
                        if (StringUtils.isNotBlank(log)){
                            write(logs, logFileName, log);
                        }
                    }
                    if (!stack.errorLogsIsEmpty()){
                        String log = stack.errorPop();
                        if (StringUtils.isNotBlank(log)){
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
