package com.mashibing.activiti.b_first;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-06-23 22:33
 */
public class ActivitiQuick  extends ApplicationTests {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    @Test
    public void createTable() {

    }

    @Test
    public void deply() {
        String bpmnName = "first";
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addClasspathResource("processes/" + bpmnName + ".bpmn")
                .addClasspathResource("processes/" + bpmnName + ".png")
                .category("HR")
                .deploy();

        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署名称\t"+deployment.getName());

    }
    @Test
    public void start() {
        String processDefinitionKey = "holidy";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID\t"+processInstance.getId());
        System.out.println("流程定义ID\t"+processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t"+processInstance.getProcessDefinitionKey());
        System.out.println("流程部署ID\t"+processInstance.getDeploymentId());
    }
    @Test
    public void findMyTask(){
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        for (Task task : taskList) {
            System.out.println("任务ID\t" + task.getId());
            System.out.println("任务名称\t" + task.getName());
            System.out.println("流程定义ID\t" + task.getProcessDefinitionId());
            System.out.println("流程实例ID\t"+task.getProcessInstanceId());
            System.out.println("执行对象ID\t"+task.getExecutionId());
            System.out.println("任务创办时间\t"+task.getCreateTime());
            System.out.println("=======");
        }
    }

    @Test
    public void compleTask() {
        taskService.complete("7505");

    }
}
