package com.mashibing.activiti.k_grouptask;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-30 22:33
 */
public class GroupTask2Test extends ApplicationTests {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    private final String bpmnNameAndKey = "groupTask2";

    @Test
    public void deploy() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/" + bpmnNameAndKey + ".bpmn")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".png")
                .key(bpmnNameAndKey)
                .name(bpmnNameAndKey + "Name")
                .deploy();

        System.out.println("部署ID\t" + deploy.getId());
        System.out.println("部署key\t" + deploy.getKey());
        System.out.println("部署name\t" + deploy.getName());
    }

    @Test
    public void start() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userIds","大a,大b,大c,大d");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey, variables);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
    }
    @Test
    public void findMyPersonTask() {
        String candidate = "大a";
        List<Task> list =
                taskService.createTaskQuery().taskAssignee(candidate).list();
        for (Task task : list) {
            System.out.println("任务ID\t" + task.getId());
            System.out.println("任务名称\t" + task.getName());
            System.out.println("任务办理人\t" + task.getAssignee());
            System.out.println("流程实例ID\t" + task.getProcessInstanceId());
            System.out.println("流程定义ID\t" + task.getProcessDefinitionId());
            System.out.println("执行对象ID\t" + task.getExecutionId());

        }
    }
    @Test
    public void findMyGroupTask() {
        String candidate = "大a";
        List<Task> list =
                taskService.createTaskQuery().taskCandidateOrAssigned(candidate).list();
        for (Task task : list) {
            System.out.println("任务ID\t" + task.getId());
            System.out.println("任务名称\t" + task.getName());
            System.out.println("任务办理人\t" + task.getAssignee());
            System.out.println("流程实例ID\t" + task.getProcessInstanceId());
            System.out.println("流程定义ID\t" + task.getProcessDefinitionId());
            System.out.println("执行对象ID\t" + task.getExecutionId());

        }
    }

    /*查询正在执行的任务办理人表*/
    @Test
    public void findRunPersonTask() {
        String taskId = "67505";
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        if (!CollectionUtils.isEmpty(identityLinksForTask)) {
            for (IdentityLink identityLink : identityLinksForTask) {
                System.out.println(identityLink.getTaskId());
                System.out.println(identityLink.getType());
                System.out.println("***********************");
            }
        }
    }

    /*查询历史任务的办理人表*/
    @Test
    public void findHisPersonTask() {
        String processInstanceId = "67501";
        List<HistoricIdentityLink> list = historyService.getHistoricIdentityLinksForProcessInstance(processInstanceId);
        for (HistoricIdentityLink historicIdentityLink : list) {
            System.out.println(historicIdentityLink.getUserId());
            System.out.println(historicIdentityLink.getType());
            System.out.println(historicIdentityLink.getTaskId());
            System.out.println("*****************************");
        }
    }

    @Test
    public void claim() {
        String taskId = "22506";
        String userId = "大a";
        taskService.claim(taskId, userId);
    }
    @Test
    public void complete() {
        String taskId = "12505";
        taskService.complete(taskId);
        System.out.println("任务完成");

    }
    @Test
    public void setAssignee() {
        String taskId = "67505";
        taskService.claim(taskId, null);
    }

    @Test
    public void addGroupUser() {
        String taskId = "22506";
        String userId = "大a";
        taskService.addCandidateUser(taskId, userId);
    }

    @Test
    public void deleteGroupUser() {
        String taskId = "12505";
        String userId = "小x";
        taskService.deleteCandidateUser(taskId, userId);

    }
}
