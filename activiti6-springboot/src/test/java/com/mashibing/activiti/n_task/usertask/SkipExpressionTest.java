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
 * <p>跳过表达式，测试类</p>
 *
 * @author 孙志强
 * @date 2020-08-28 23:06
 */
public class SkipExpressionTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    private final String bpmnNameAndKey = "skipExpression";

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
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstUser", "张三");
        variables.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey, variables);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t" + processInstance.getProcessDefinitionKey());
    }

    /* 测试跳过表达式，当 某一个流程的办理人，是流程的发起人的时候，该任务节点，对于大多数业务流程，是不再需要审核的，自动执行，也就是跳过 */
    @Test
    public void compleTask() {
        String taskId = "2507";
        Map<String, Object> variables = new HashMap<>();
        variables.put("inputUser", "张三");
        taskService.complete(taskId, variables);
    }

}
