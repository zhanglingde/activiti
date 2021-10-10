package com.mashibing.activiti.q_drools;

import com.alibaba.fastjson.JSON;
import com.mashibing.activiti.ApplicationTests;
import com.mashibing.activiti.entity.Order;
import com.mashibing.activiti.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.junit.jupiter.api.Test;

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
public class DroolsProcessTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private ProcessEngineConfiguration processEngineConfiguration;


    private final String bpmnNameAndKey = "point";

    @Test
    public void deployment() {
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".bpmn")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".png")
                .addClasspathResource("rules/Point-rules.drl")
                .category("HR")
                .deploy();
        System.out.println("部署ID\t" + deployment.getId());
        System.out.println("部署分类\t" + deployment.getCategory());
        System.out.println("部署名称\t" + deployment.getName());
    }

    @Test
    public void start() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t" + processInstance.getProcessDefinitionKey());
    }

    @Test
    public void compleTask() {
        String taskId = "2505";

        Map<String, Object> variables = new HashMap<>();

        Order order = new Order();
        order.setAmout(400);
        User user = new User();
        user.setLevel(1);
        user.setName("Name1");
        order.setUser(user);
        variables.put("point1", order);

        taskService.complete(taskId,variables);

    }

    @Test
    public void getVariable(){
        Object result = runtimeService.getVariable("2502", "result");
        System.out.println(JSON.toJSONString(result));
    }

    /*保存会员操作接口*/
    @Test
    public void test1() {
        /*1- 数据库验证*/
        /*幂等操作*/
        //用身份证建一个redis key


        //select count(*) from user where username ='张三'
        //0
        /*2- 保存*/
//        Insert
    }



}
