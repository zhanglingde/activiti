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
import java.util.List;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-30 22:33
 */
public class GroupTask1Test extends ApplicationTests {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    private final String bpmnNameAndKey = "groupTask1";

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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
    }
    @Test
    public void findMyPersonTask() {
        String candidate = "小a";
        List<Task> list =
                taskService.createTaskQuery()
                        .taskAssignee(candidate)
                        .list();
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
        String candidate = "小d";
        List<Task> list =
                taskService.createTaskQuery()
                        /* 查询任务代理人 */
//                        .taskAssignee(candidate)
                        /* 查询任务拥有者 */
//                        .taskOwner()
                        /*  查询任务候选人*/
                        .taskCandidateUser(candidate)
//                        .taskCandidateOrAssigned(candidate)
//                        .taskCandidateUser(candidate)
                        .list();
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
    public void complete() {
        String taskId = "7505";
        taskService.complete(taskId);
        System.out.println("任务完成");

    }

    /*查询正在执行的任务办理人表*/
    @Test
    public void findRunPersonTask() {
        String taskId = "7505";
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
        String processInstanceId = "2501";
        List<HistoricIdentityLink> list = historyService.getHistoricIdentityLinksForProcessInstance(processInstanceId);
        for (HistoricIdentityLink historicIdentityLink : list) {
            System.out.println(historicIdentityLink.getUserId());
            System.out.println(historicIdentityLink.getType());
            System.out.println(historicIdentityLink.getTaskId());
            System.out.println("*****************************");
        }
    }

    /* 任务拾取-把组任务变成个人任务 */
    @Test
    public void claim() {
        String taskId = "7505";
        String userId = "小x";
        taskService.claim(taskId, userId);
    }

    /* 把个人任务规归还到组任务 */
    @Test
    public void setAssignee() {
        String taskId = "7505";
        taskService.claim(taskId, null);
    }

    @Test
    public void addGroupUser() {
        String taskId = "7505";
        String userId = "小x";
        taskService.addCandidateUser(taskId, userId);
    }

    @Test
    public void deleteGroupUser() {
        String taskId = "7505";
        String userId = "小a";
        taskService.deleteCandidateUser(taskId, userId);

    }
}
