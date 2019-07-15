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
                File logs = new File(savePath + saveFolderName + Log_File_Name);
                File errorLogs = new File(savePath + saveFolderName + Error_Log_File_Name);
                while (true){
                        try(FileWriter writer = new FileWriter(logs,true);
                        FileWriter errorWriter = new FileWriter(errorLogs,true)){
                            if (!stack.logsIsEmpty()){
                                String log = stack.poll();
                                if (StringUtils.isNotBlank(log)){
                                    writer.write(log);
                                }
                            }
                            if (!stack.errorLogsIsEmpty()){
                                String log = stack.errorPop();
                                if (StringUtils.isNotBlank(log)){
                                    errorWriter.write(log);
                                }
                            }
                        }catch (Exception e){
                            stack.errorAdd(e.getMessage());
                        }
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
