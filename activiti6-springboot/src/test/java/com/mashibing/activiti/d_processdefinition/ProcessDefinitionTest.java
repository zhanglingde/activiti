package com.mashibing.activiti.d_processdefinition;

import com.alibaba.fastjson.JSON;
import com.mashibing.activiti.ApplicationTests;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 *     RepositoryService 跟两件事有关
 *  第一个：部署对象
 *  第二个：流程定义
 *
 * </p>
 *
 * @author 孙志强
 * @date 2020-07-21 21:13
 */
public class ProcessDefinitionTest extends ApplicationTests {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;

    private final String bpmnNameAndKey = "first";
    private String tenantId = "mashibing-activiti6";
    /*通过classpath路径下的资源文件部署流程*/
    @Test
    public void deploymentClassPath() {
        Deployment deployment = repositoryService.createDeployment()
                .name("请假流程")
                .key(bpmnNameAndKey)
                .tenantId(tenantId)
                .addClasspathResource("processes/" + bpmnNameAndKey + ".bpmn")
//                .addClasspathResource("processes/" + bpmnNameAndKey + ".png")
                .category("HR")
                .deploy();

        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署名称\t"+deployment.getName());

    }
    /*通过zip压缩包部署流程*/
    @Test
    public void deploymentZip() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("processes/first.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addZipInputStream(zipInputStream)
                .category("HR")
                .deploy();

        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署名称\t"+deployment.getName());
    }
    /*通过输入流部署流程*/
    @Test
    public void deploymentInputstream() {
//        注意区别
//        this.getClass().getClassLoader().getResourceAsStream("processes/first.bpmn");
//        this.getClass().getResourceAsStream("/processes/first.bpmn");
//        this.getClass().getResourceAsStream("processes/first.bpmn");
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("processes/first.bpmn");
        InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("processes/first.png");
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addInputStream("first.bpmn",inputStreamBpmn)
                .addInputStream("first.png",inputStreamPng)
                .category("HR")
                .deploy();

        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署名称\t"+deployment.getName());
    }
    /*通过BpmnModel部署流程*/
    @Test
    public void deploymentModel() {
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addBpmnModel("first", createBpmnModel())
                .category("HR")
                .deploy();

        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署名称\t"+deployment.getName());
    }
    public BpmnModel createBpmnModel(){
        BpmnModel bpmnModel = new BpmnModel();
        //流程定义
        Process process = new Process();
        process.setId("first");
        process.setName("firstProcess");
        bpmnModel.addProcess(process);
        //开始
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);
        //用户任务
        UserTask userTask = new UserTask();
        userTask.setId("usertask1");
        userTask.setName("审批【部门经理】");
        process.addFlowElement(userTask);
        //用户任务结束
        EndEvent endEvent = new EndEvent();
        startEvent.setId("endEvent");
        process.addFlowElement(startEvent);
        //连线
        process.addFlowElement(new SequenceFlow("startEvent", "usertask1"));
        process.addFlowElement(new SequenceFlow("usertask1", "endEvent"));
        return bpmnModel;

    }

    @Test
    public void startByKey(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义key\t" + processInstance.getProcessDefinitionKey());
    }
    @Test
    public void startByKeyAndTenantId(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(bpmnNameAndKey, tenantId);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义key\t" + processInstance.getProcessDefinitionKey());
    }
    @Test
    public void findDeployment() {
        Deployment deployment = repositoryService.createDeploymentQuery()
                .processDefinitionKey(bpmnNameAndKey)
//                .latest()
                .singleResult();

        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署名称\t"+deployment.getName());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署KEY\t"+deployment.getKey());
        System.out.println("部署租户tenantID\t"+deployment.getTenantId());
        System.out.println("部署时间\t"+deployment.getDeploymentTime());
    }

    @Test
    public void findDefinition() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(bpmnNameAndKey)
                .latestVersion()
                 .singleResult();

        System.out.println("流程定义ID\t"+processDefinition.getId());
        System.out.println("部署ID\t"+processDefinition.getDeploymentId());
        System.out.println("资源名称\t"+processDefinition.getResourceName());
        System.out.println("资源图片名称\t"+processDefinition.getDiagramResourceName());
    }

    @Test
    public void delete() {
//        String deploymentId = "2501";
//        repositoryService.deleteDeployment(deploymentId, true);

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(bpmnNameAndKey).list();
        for (ProcessDefinition processDefinition : list) {
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
        }
    }

    @Test
    public void getPic() throws Exception {
        String deploymentId = "1";
        String resourceName = "processes/first.png";
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        FileCopyUtils.copy(inputStream, new FileOutputStream(new File("D:/123.png")));
    }

    @Test
    public void findLatestVersion() {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc().list();

        LinkedHashMap<String, ProcessDefinition> map = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(definitions)) {
            for (ProcessDefinition definition : definitions) {
                map.put(definition.getKey(), definition);
            }
        }

        ArrayList<ProcessDefinition> list = new ArrayList<>(map.values());
        for (ProcessDefinition processDefinition : list) {
            System.out.println("流程定义ID\t"+processDefinition.getId());
            System.out.println("部署ID\t"+processDefinition.getDeploymentId());
            System.out.println("资源名称\t"+processDefinition.getResourceName());
            System.out.println("资源图片名称\t"+processDefinition.getDiagramResourceName());
        }
    }

    @Test
    public void fingBpmnModel() {
        String processDefinitionId = "first:1:4";
        //获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
        for (Map.Entry<String, GraphicInfo> entry : locationMap.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println("==============");

        Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
        for (Map.Entry<String, List<GraphicInfo>> entry : flowLocationMap.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    /**/
    @Test
    public void suspend() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(bpmnNameAndKey)
                .latestVersion()
                .singleResult();
        boolean suspended = processDefinition.isSuspended();
        System.out.println(suspended ? "流程被暂停状态" : "流程激活状态");
        String processDefinitionId = processDefinition.getId();
        if(suspended){
            System.out.println("流程定义："+processDefinitionId+"当前暂停");
        }else{
            repositoryService.suspendProcessDefinitionById(processDefinitionId,true,null);
            System.out.println("流程定义："+processDefinitionId+"挂起");
        }
    }
    @Test
    public void activate() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(bpmnNameAndKey)
                .latestVersion()
                .singleResult();
        boolean suspended = processDefinition.isSuspended();
        System.out.println(suspended ? "流程被暂停状态" : "流程激活状态");
        String processDefinitionId = processDefinition.getId();
        if(!suspended){
            //说明是暂停，就可以激活操作
            System.out.println("流程定义："+processDefinitionId+"已经激活");
        }else{
            repositoryService.activateProcessDefinitionById(processDefinitionId,true
                    ,null);
            System.out.println("流程定义："+processDefinitionId+"激活成功");
        }
    }

}
