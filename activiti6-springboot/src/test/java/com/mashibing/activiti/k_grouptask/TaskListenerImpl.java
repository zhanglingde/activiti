package com.mashibing.activiti.k_grouptask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import javax.mail.Session;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-29 23:45
 */
public class TaskListenerImpl implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        /*指定个人任务的办理人，组任务办理人*/

        /*  */
        /*可以查询数据库*/
        delegateTask.addCandidateUser("小a");
        delegateTask.addCandidateUser("小b");
    }
}
