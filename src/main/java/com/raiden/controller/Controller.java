package com.raiden.controller;

import com.raiden.handler.TaskHandler;
import com.raiden.logs.Logger;
import com.raiden.task.Task;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 控制器
 */
public class Controller {

    private static final String CONTENT_TEXT = "请输入要比较的json。";

    private Queue<Task> taskStack;
    private Lock lock;
    private Condition condition;
    private TaskHandler handler;
    private Logger logger = Logger.newInstance();

    public Controller(){
        this.taskStack = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
        this.handler = new TaskHandler();
    }

    /**
     * 添加任务并且唤醒主线程
     */
    public void add(Task task){
        taskStack.add(task);
        signal();
    }

    /**
     * 主要工作线程
     */
    public void start(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try{
                    while (true){
                        try {
                            if (taskStack.isEmpty()){
                                condition.await();
                            }else {
                                Task task = taskStack.poll();
                                handler.handler(task);
                            }
                        }catch (Throwable e){
                            logger.error(e);
                            //为了防止主线程因为解析报错而中断
                            e.printStackTrace();
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * 唤醒主线程
     */
    private void signal(){
        lock.lock();
        try {
            condition.signal();
        }finally {
            lock.unlock();
        }
    }
}
