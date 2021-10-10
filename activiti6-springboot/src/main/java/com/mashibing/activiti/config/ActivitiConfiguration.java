package com.mashibing.activiti.config;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.rules.RulesDeployer;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author:孙志强
 * @date 2020/09/30 17:36
 * @Modified BY:
 **/

@Component
public class ActivitiConfiguration implements ProcessEngineConfigurationConfigurer {


	@Override
	public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
		/* 小坑：监听器是自己写构造注入，没有加入spring管理，加入spring后，一些activiti的service 注入不进去 */
		TaskService taskService = processEngineConfiguration.getTaskService();
		MyEventListener myEventListener = new MyEventListener(taskService);
		processEngineConfiguration.setActivityFontName("宋体");
		processEngineConfiguration.setLabelFontName("宋体");
		processEngineConfiguration.setAnnotationFontName("宋体");

		List<Deployer> customPostDeployers = new ArrayList<>();
		customPostDeployers.add(new RulesDeployer());
		processEngineConfiguration.setCustomPostDeployers(customPostDeployers);
		processEngineConfiguration.setIdGenerator(new StrongUuidGenerator());
		List<ActivitiEventListener> eventListeners = new ArrayList<>();
//		eventListeners.add(myEventListener);
		processEngineConfiguration.setEventListeners(eventListeners);
	}
}
