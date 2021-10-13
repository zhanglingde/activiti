package com.mashibing.activiti.e_processinstance;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *     流程实例
 * </p>
 *
 * @author 孙志强
 * @date 2020-07-21 21:13
 */
public class ProcessInstanceTest extends ApplicationTests {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    private final String bpmnNameAndKey = "first";

    // 流程实例重新部署
    @Test
    public void deployment() {
        String bpmnName = "first";
        Deployment deployment = repositoryService.createDeployment()
                .name("请假流程")
                .key(bpmnNameAndKey)
                .addClasspathResource("processes/" + bpmnName + ".bpmn")
                .addClasspathResource("processes/" + bpmnName + ".png")
                .category("HR")
                .deploy();
        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署名称\t"+deployment.getName());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署KEY\t"+deployment.getKey());
        System.out.println("部署租户tenantID\t"+deployment.getTenantId());
        System.out.println("部署时间\t"+deployment.getDeploymentTime());
    }

    @Test
    public void start() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);
        System.out.println("流程实例ID\t"+processInstance.getId());
        System.out.println("流程定义ID\t"+processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t"+processInstance.getProcessDefinitionKey());
        System.out.println("启动成功");
    }

    // 查看当前任务（个人待办）
    @Test
    public void findMyTask(){
        String assignee = "李四";
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();

        for (Task task : taskList) {
            System.out.println("任务ID\t"	 + task.getId());
            System.out.println("任务名称\t" + task.getName());
            System.out.println("流程定义ID\t" + task.getProcessDefinitionId());
            System.out.println("流程实例ID\t"+task.getProcessInstanceId());
            System.out.println("执行对象ID\t"+task.getExecutionId());
            System.out.println("任务创办时间\t"+task.getCreateTime());
            System.out.println("=======");
        }
    }

    // 完成任务，完成任务后，张三查询不到当前任务，李四可以查询到当前任务
    @Test
    public void completeTask() {
        String taskId = "4e88864e-2bf3-11ec-bce7-9e96ca6eba6f";
        // 流程变量
        taskService.complete(taskId);
        System.out.println("任务完成");
    }


    // 查询历史任务
    @Test
    public void findHistoryTask() {
        String assignee = "张三";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            for (HistoricTaskInstance taskInstance : list) {
                System.out.println("任务名称\t" + taskInstance.getName()+"\t开始时间\t"+taskInstance.getStartTime());
            }
        }
    }

    @Test
    public void findRunInstance(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(bpmnNameAndKey)
                .list();
        for (ProcessInstance processInstance : list) {
            System.out.println(processInstance.getId()+"\t"+processInstance.getProcessDefinitionId());
        }
    }

    // 查询历史流程实例
    @Test
    public void findHistoryHinst(){
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(bpmnNameAndKey)
                .list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println(historicProcessInstance.getId()+"\t"+historicProcessInstance.getProcessDefinitionId());
        }
    }

    // 当前正在执行的任务
    @Test
    public void findRunExecute(){
        List<Execution> list = runtimeService.createExecutionQuery()
                .processDefinitionKey(bpmnNameAndKey)
                .list();
        for (Execution execution : list) {
            System.out.println(execution.getId()+"\t"+execution.getActivityId());
        }
    }

    // 查询流程是否结束
    @Test
    public void isProcessEnd() {
        String processInstanceId = "2501";
        long count = runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .count();
        System.out.println(count>0?"路程未结束":"流程已经结束");
    }


}
