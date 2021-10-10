package com.mashibing.activiti.a_config;

import java.util.HashMap;
import java.util.Map;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * 将history属性设置为none
 * @author 孙志强
 *
 */
public class HistoryLevelTest extends ApplicationTests {
	@Resource
	private RepositoryService repositoryService;

	@Resource
	private RuntimeService runtimeService;
	@Resource
	private TaskService taskService;


	@BeforeEach
	public void deply() {
		String bpmnName = "first";
		repositoryService.createDeployment().name("请假流程")
				.addClasspathResource("processes/" + bpmnName + ".bpmn")
                .addClasspathResource("processes/" + bpmnName + ".png")
				.deploy();

	}

	@Test
	public void testActivity() {
		//创建参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("day", 10);
		//开始流程
		ProcessInstance myProcess_1 = runtimeService.startProcessInstanceByKey("myProcess_1",vars);
	}

}
