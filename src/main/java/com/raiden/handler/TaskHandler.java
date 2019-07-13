package com.raiden.handler;

import com.raiden.task.Task;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 19:01 2019/7/13
 * @Modified By: 任务处理器
 */
public class TaskHandler {

    public void handler(Task task){
        task.execute();
    }
}
