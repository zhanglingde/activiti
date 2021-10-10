package com.mashibing.activiti.n_task.usertask;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-08-28 23:06
 */
public class UserTaskTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    private final String bpmnNameAndKey = "userTask";

    @Test
    public void deployment() {
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".bpmn")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".png")
                .category("HR")
                .deploy();
        System.out.println("部署ID\t" + deployment.getId());
        System.out.println("部署分类\t" + deployment.getCategory());
        System.out.println("部署名称\t" + deployment.getName());
    }

    @Test
    public void start() throws InterruptedException {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t" + processInstance.getProcessDefinitionKey());
    }

    /*  如果设置了超期，activiti只会在Due Date上协商时间，代表任务超期，但是不会做其他事情 */
    /* 超期提醒怎么做??? 分布式任务调度框架--很重要 */
    @Test
    public void findTask() {

        Date date = new Date();
        List<Task> taskList = taskService.createTaskQuery()
                .taskDueBefore(date)
                .list();

        for (Task task : taskList) {
            System.out.println("任务ID\t" + task.getId());
            System.out.println("任务名称\t" + task.getName());
            System.out.println("流程定义ID\t" + task.getProcessDefinitionId());
            System.out.println("流程实例ID\t" + task.getProcessInstanceId());
            System.out.println("执行对象ID\t" + task.getExecutionId());
            System.out.println("任务创办时间\t" + task.getCreateTime());
            System.out.println("=======");
        }
    }

    @Test
    public void compleTask() {
        String taskId = "2505";
        Map<String, Object> variables = new HashMap<>();
        variables.put("inputUserList", Arrays.asList("李四", "王五"));
        taskService.complete(taskId, variables);
    }

}
