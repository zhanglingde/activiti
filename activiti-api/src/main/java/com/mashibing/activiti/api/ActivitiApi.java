package com.mashibing.activiti.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ${DESCRIPTION}:TODO
 *
 * @author:孙志强
 * @create:2018-12-24 15:56
 * @version:v1.0
 * @Modified BY:
 * @修改人和其它信息:
 **/
@FeignClient(value = "MASHIBING-ACTIVITI",fallbackFactory= ActivitiApiFallbackFactory.class)
public interface ActivitiApi {

	@RequestMapping(value = "/activiti/deploymentProcessDefinition/{deploymentName}/{categoryId}/{bpmnFileName}")
	R deploymentProcessDefinition(@PathVariable("deploymentName") String deploymentName,
								  @PathVariable("categoryId") String categoryId,
								  @PathVariable("bpmnFileName") String bpmnFileName);

	@RequestMapping(value = "/activiti/deleteProcessDefinitionByKey/{processDefinitionKey}/{cascade}")
	R deleteProcessDefinitionByKey(@PathVariable("processDefinitionKey") String processDefinitionKey, @PathVariable("cascade") Boolean cascade);

	@RequestMapping(value = "/activiti/viewPic/{categoryId}")
	R viewPic(@PathVariable("categoryId") String categoryId) ;

	@RequestMapping("/activiti/startProcess/{businessKey}/{userId}/{processDefinitionKey}/{variables}")
	R startProcess(@PathVariable("businessKey") String businessKey, @PathVariable("userId") String userId, @PathVariable("processDefinitionKey") String processDefinitionKey, @PathVariable("variables") String variables);

	@RequestMapping("/activiti/getTasksByUserId/{userId}")
	R getTasksByUserId(@PathVariable("userId") String userId);

	@RequestMapping("/activiti/completeTask/{businessKey}/{userId}/{variables}")
	R completeTask(@PathVariable("businessKey") String businessKey, @PathVariable("userId") String userId, @PathVariable("variables") String variables);

	@RequestMapping("/activiti/getOutComeListByBusinessKey/{businessKey}/{userId}")
	R getOutComeListByBusinessKey(@PathVariable("businessKey") String businessKey, @PathVariable("userId") String userId);

	@RequestMapping("/activiti/getCommonList/{businessKey}/{userId}")
	R getCommonList(@PathVariable("businessKey") String businessKey, @PathVariable("userId") String userId);

	@RequestMapping("/activiti/getHistoryTask/{userId}")
	R getHistoryTask(@PathVariable("userId") String userId);

	@RequestMapping("/activiti/getHistoryProcessInstance")
	R getHistoryProcessInstance();

	@RequestMapping(value = "/activiti/viewCurrentPic/{businessKey}")
	R viewCurrentPic(@PathVariable("businessKey") String businessKey) ;

	@RequestMapping(value = "/activiti/getCoording/{businessKey}")
	R getCoording(@PathVariable("businessKey") String businessKey);



}
