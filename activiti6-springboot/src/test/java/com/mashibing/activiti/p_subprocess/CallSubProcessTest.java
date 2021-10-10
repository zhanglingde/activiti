package com.mashibing.activiti.p_subprocess;

import com.mashibing.activiti.ApplicationTests;
import com.mashibing.activiti.k_grouptask.GroupTask1Test;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-09-14 20:56
 */
public class CallSubProcessTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    private final String bpmnNameAndKey1 = "callSubProcess";
    private final String bpmnNameAndKey2 = "mainProcess";

    @Test
    public void deployment() {
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addClasspathResource("processes/" + bpmnNameAndKey1 + ".bpmn")
                .addClasspathResource("processes/" + bpmnNameAndKey1 + ".png")

                .addClasspathResource("processes/" + bpmnNameAndKey2 + ".bpmn")
                .addClasspathResource("processes/" + bpmnNameAndKey2 + ".png")
                .category("HR")
                .deploy();
        System.out.println("部署ID\t" + deployment.getId());
        System.out.println("部署分类\t" + deployment.getCategory());
        System.out.println("部署名称\t" + deployment.getName());
    }

    @Test
    public void start() {
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey2);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程实例ID\t" + processInstance.getProcessInstanceId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
    }

    @Test
    public void compleTask() {
        String taskId = "2505";
        taskService.complete(taskId);

    }
}
