package com.mashibing.activiti.n_task.receivetask;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-27 22:33
 */
public class ReceiveTaskTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    private final String bpmnNameAndKey = "receiveTask";
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
    public void start() {
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);
        System.out.println("流程实例ID\t"+processInstance.getId());
        System.out.println("流程实例ID\t"+processInstance.getProcessInstanceId());
        System.out.println("流程定义ID\t"+processInstance.getProcessDefinitionId());

        //查询执行对象ID
        Execution receivetask1 = runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .activityId("receivetask1").singleResult();

        /*使用流程变量设置当日销售饿，传递业务参数*/
        runtimeService.setVariable(receivetask1.getId(), "汇总当日销售额", 2000);
        /*向后执行一步*/
        runtimeService.trigger(receivetask1.getId());

        //查询执行对象ID
        Execution receivetask2= runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .activityId("receivetask2").singleResult();

        /*使用流程变量设置当日销售饿，传递业务参数*/
        Integer value = (Integer) runtimeService.getVariable(receivetask1.getId(), "汇总当日销售额");

        System.out.println("给老板发送短信：金额是" + value);

        /*向后执行一步*/
        runtimeService.trigger(receivetask2.getId());

    }

}
