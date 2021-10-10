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
 * <p></p>
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

    @Test
    public void deployment() {
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
        String assignee = "lisi";
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
//                .taskCandidateUser()
//                .processDefinitionId()
//                .list()
//                .count()

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

    @Test
    public void compleTask() {
        taskService.complete("20002");

    }

    @Test
    public void isProcessEnd() {
        String processInstanceId = "17501";
        List<Execution> list = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        if (null == list) {
            System.out.println("流程结束");
        }else{
            System.out.println("流程未结束");
        }
    }

    @Test
    public void findHistoryTask() {
        String assignee = "zhangsan";
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
    public void findHistoryHinst(){
        String processInstanceId = "17501";
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        System.out.println(historicProcessInstance.getId()+"\t"+historicProcessInstance.getProcessDefinitionId());

    }

}
