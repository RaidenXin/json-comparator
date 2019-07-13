package com.raiden.logs;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogsStack {

    private static final Queue<String> queue = new ConcurrentLinkedQueue<>();
    private static final Queue<String> error_queue = new ConcurrentLinkedQueue<>();
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static final LogsStack instance = new LogsStack();

    private LogsStack(){
    }

    public static final LogsStack newInstance(){
        return instance;
    }

    public void push(String log){
        push(log, queue);
    }

    private void push(String log,Queue stack){
        lock.lock();
        try{
            log = log + "\r\n";
            stack.add(log);
            condition.signal();
        }catch (Exception e){
            stack.add(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    public String poll(){
        String result = null;
        try{
            result = queue.poll();
        }catch (Exception e){
            queue.add(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public void errorAdd(String log){
        push(log, error_queue);
    }

    public String errorPop(){
        String result = null;
        try{
            result = error_queue.poll();
        }catch (Exception e){
            error_queue.add(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public boolean logsIsEmpty(){
        isAwait();
        return queue.isEmpty();
    }

    public boolean errorLogsIsEmpty(){
        isAwait();
        return error_queue.isEmpty();
    }

    private void isAwait(){
        lock.lock();
        try{
            if (queue.isEmpty() && error_queue.isEmpty()){
                condition.await();
            }
        }catch (Exception e){
            error_queue.add(e.getMessage());
        }finally {
            lock.unlock();
        }
    }
}
