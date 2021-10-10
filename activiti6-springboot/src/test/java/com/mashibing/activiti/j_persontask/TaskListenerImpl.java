package com.mashibing.activiti.j_persontask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-29 23:45
 */
@Component
public class TaskListenerImpl implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        /*指定个人任务的办理人，组任务办理人*/
        /*可以查询数据库*/
        delegateTask.setAssignee("张三");

    }
}
