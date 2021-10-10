package com.mashibing.activiti.service;

import com.alibaba.fastjson.JSON;
import com.mashibing.activiti.ApplicationTests;
import com.mashibing.activiti.api.R;
import com.mashibing.activiti.api.TaskRes;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author sunzhiqiang23
 * @date 2020-10-10 21:30
 */
public class ActivitiServiceTest extends ApplicationTests {

    @Resource
    private ActivitiService activitiService;

    @Test
    void deploymentProcessDefinition() {
        Deployment deployment = activitiService.deploymentProcessDefinition("请假流程", "人资", "leave");
        System.out.println(JSON.toJSONString(deployment));

    }

    @Test
    void viewPic() throws IOException {
        String filePath = "D://请假流程.png";
        InputStream inputStream = activitiService.viewPic("人资");
        FileUtils.copyInputStreamToFile(inputStream, new File(filePath));
    }

    @Test
    void startProcess() {
        String bussnessId = "loan-1";
        String bpmnNameAndKey = "leave";
        Map<String, Object> variables = new HashMap<>();
        variables.put("inputUser", "张三");
        activitiService.startProcess(bussnessId, bpmnNameAndKey,JSON.toJSONString(variables));
    }

    @Test
    void getTasksByUserId() {
        String bussnessId = "loan-1";
        String userId = "张三";
        List<TaskRes> tasks = activitiService.getTasksByUserId(bussnessId, userId);
        System.out.println(JSON.toJSONString(tasks));
    }

    @Test
    void getOutComeListByBusinessKey() {
        String bussnessId = "loan-1";
        R r = activitiService.getOutComeListByBusinessKey(bussnessId);
        System.out.println(r);
    }


    @Test
    void getCommonList() {
        String bussnessId = "loan-1";
        R commonList = activitiService.getCommonList(bussnessId);
        System.out.println(commonList);
    }

    @Test
    void completeTask() {
        String bussnessId = "loan-1";
        Map<String, Object> variables = new HashMap<>();
        variables.put("currentUser", "王五");
//        variables.put("inputUser", "王五");
        variables.put("outcome", "批准");
//        variables.put("common", "我想请假");
        R r = activitiService.completeTask(bussnessId, JSON.toJSONString(variables));
        System.out.println(r);
    }
    @Test
    void getHistoryTask() {
        String userId = "李四";
        R historyTask = activitiService.getHistoryTask(userId);
        System.out.println(historyTask);
    }

    @Test
    void getHistoryProcessInstance() {
        R historyProcessInstance = activitiService.getHistoryProcessInstance();
        System.out.println(JSON.toJSONString(historyProcessInstance));
    }

    @Test
    void deleteProcessInstance() {
        String businessKey = "loan-1";
        String reason = "测试删除";
        R r = activitiService.deleteProcessInstance(businessKey, reason);
        System.out.println(r);
    }

    @Test
    void viewCurrentPic() throws IOException {
        String businessKey = "loan-1";
        String filePath = "D://请假流程.png";
        InputStream inputStream = activitiService.viewCurrentPic(businessKey);
        FileUtils.copyInputStreamToFile(inputStream, new File(filePath));
    }
}