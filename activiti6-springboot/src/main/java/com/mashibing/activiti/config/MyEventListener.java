package com.mashibing.activiti.config;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>activiti监听器</p>
 *  在流程监听器上走一些事情，比如任务完成，发送mq，发送办理人消息
 * @author 孙志强
 * @date 2020-08-27 22:19
 */
public class MyEventListener implements ActivitiEventListener{
    private TaskService taskService;

    public MyEventListener(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {

            case TASK_COMPLETED:
                System.out.println("有任务执行人。。。");
                System.out.println(event.getProcessInstanceId());
                List<Task> list = taskService.createTaskQuery()
                        .processInstanceId(event.getProcessInstanceId())
                        .active()
                        .list();
                if (!CollectionUtils.isEmpty(list)) {
                    for (Task task : list) {
                        System.out.println(task.getName());
                        System.out.println(task.getAssignee());
                        /* 能查到任务，就能发送通知 */
                    }
                }
                break;
            case TASK_ASSIGNED:
                break;
            case TASK_CREATED:
                break;

            default:
//                System.out.println("Event received: " + event.getType());
        }
    }

    /**
     * @return whether or not the current operation should fail when this listeners execution throws an exception.
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }
}
