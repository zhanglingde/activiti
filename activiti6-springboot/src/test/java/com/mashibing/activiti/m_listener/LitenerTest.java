package com.mashibing.activiti.m_listener;

import com.mashibing.activiti.ApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-27 22:33
 */
@Slf4j
public class LitenerTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;


    private final String bpmnNameAndKey = "listener";

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
    public void start() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("inputUser", "张三");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey, variables);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t" + processInstance.getProcessDefinitionKey());
    }

    @Test
    public void findMyTask() {
        String assignee = "张三";
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();

        for (Task task : taskList) {
            System.out.println("任务ID\t" + task.getId());
            System.out.println("任务名称\t" + task.getName());
            System.out.println("流程定义ID\t" + task.getProcessDefinitionId());
            System.out.println("流程实例ID\t" + task.getProcessInstanceId());
            System.out.println("执行对象ID\t" + task.getExecutionId());
            System.out.println("任务创办时间\t" + task.getCreateTime());
            System.out.println("=======");
        }
    }

    @Test
    public void compleTask() {
        String taskId = "7506";
        Map<String, Object> variables = new HashMap<>();
        variables.put("inputUser", "李四");
        variables.put("message", "通过");
//        ${message=='通过'}
        taskService.complete(taskId,variables);

    }


}
