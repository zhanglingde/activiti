package com.mashibing.activiti.o_bpmn.b_end;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * <p>取消结束事件</p>
 *
 * @author 孙志强
 * @date 2020-07-27 22:33
 */
public class CancelErrorTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    private final String bpmnNameAndKey = "endCancel";
    @Test
    public void deployment() {
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".bpmn")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".png")
                .category("HR")
                .deploy();
        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署名称\t"+deployment.getName());
    }
    @Test
    public void startAndComplete() throws InterruptedException {
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        System.out.println("任务名称\t"+task.getName());

        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        System.out.println("任务名称\t"+task.getName());
    }



}
