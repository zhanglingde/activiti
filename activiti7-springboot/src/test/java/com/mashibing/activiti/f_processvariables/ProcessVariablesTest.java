package com.mashibing.activiti.f_processvariables;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-21 21:13
 */
public class ProcessVariablesTest extends ApplicationTests {

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
        String bpmnName = "processVariables";
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
        String processDefinitionKey = "processVariables";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID\t"+processInstance.getId());
        System.out.println("流程定义ID\t"+processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t"+processInstance.getProcessDefinitionKey());
        System.out.println("流程部署ID\t"+processInstance.getDeploymentId());
    }
    @Test
    public void setAndGetVariables() {

    }
    @Test
    public void setVariables() {
        String taskId = "32502";
        taskService.setVariableLocal(taskId, "请假天数", 1);
        taskService.setVariable(taskId, "请假日期", new Date());
        taskService.setVariable(taskId, "请假原因", "回家探亲");
        System.out.println("设置流程变量成功");
    }
    @Test
    public void getVariables() {
        String taskId = "32502";
        Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
        Date date = (Date) taskService.getVariable(taskId, "请假日期");
        String reason = (String) taskService.getVariable(taskId, "请假原因");
        System.out.println(days + "\t" + date + "\t" + reason);
    }

    @Test
    public void findMyTask(){
        String assignee = "李四";
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
        String taskId = "32502";
        taskService.complete(taskId);

    }

    @Test
    public void findHistoryVariable() {
        String processInstanceId = "27501";
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            for (HistoricVariableInstance taskInstance : list) {
                System.out.println("任务名称\t" + taskInstance.getVariableName()+"\t开始时间\t"+taskInstance.getValue());
            }
        }
    }

}
