package com.mashibing.activiti.d_processdefinition;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-21 21:13
 */
public class ProcessDefinitionTest extends ApplicationTests {

    @Resource
    private RepositoryService repositoryService;

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
    public void findDeployment() {
        String name = "请假流程";
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentName(name).singleResult();

        System.out.println("部署ID\t"+deployment.getId());
        System.out.println("部署分类\t"+deployment.getCategory());
        System.out.println("部署名称\t"+deployment.getName());
    }

    @Test
    public void findDefinition() {
        String key = "holidy";
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();

        System.out.println("流程定义ID\t"+processDefinition.getId());
        System.out.println("部署ID\t"+processDefinition.getDeploymentId());
        System.out.println("资源名称\t"+processDefinition.getResourceName());
        System.out.println("资源图片名称\t"+processDefinition.getDiagramResourceName());
    }

    @Test
    public void delete() {
//        String deploymentId = "2501";
//        repositoryService.deleteDeployment(deploymentId, true);

        String key = "holidy";
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).list();
        for (ProcessDefinition processDefinition : list) {
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
        }
    }

    @Test
    public void getPic() throws Exception {
        String deploymentId = "2501";
        String resourceName = "processes/first.png";
        InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        FileCopyUtils.copy(resourceAsStream, new FileOutputStream(new File("D:/123.png")));
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


}
