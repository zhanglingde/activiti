package com.mashibing.activiti.api;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author 孙志强
 */
@Component
public class ActivitiApiFallbackFactory implements FallbackFactory<ActivitiApi> {
	@Override
	public ActivitiApi create(Throwable throwable) {
		return new ActivitiApi() {
			@Override
			public R deploymentProcessDefinition(String deploymentName,String ca, String bpmnFileName) {
				return R.error("流程部署文件："+bpmnFileName+"出错，错误信息："+throwable.getMessage());
			}
			@Override
			public R deleteProcessDefinitionByKey(String processDefinitionKey, Boolean cascade) {
				return R.error();
			}

			@Override
			public R viewPic(String categoryId) {
				return R.error();
			}

			@Override
			public R startProcess(String businessKey, String userId, String processDefinitionKey,String variables) {
				return R.error();
			}

			@Override
			public R getTasksByUserId(String userId) {
				return R.error();
			}
			@Override
			public R completeTask(String businessKey, String userId, String jsonParamStr) {
				return R.error();
			}

			@Override
			public R getOutComeListByBusinessKey(String businessKey, String userId) {
				return R.error();
			}

			@Override
			public R getCommonList(String businessKey, String userId) {
				return R.error();
			}

			@Override
			public R getHistoryTask(String userId) {
				return R.error();
			}

			@Override
			public R getHistoryProcessInstance() {
				return R.error();
			}

			@Override
			public R viewCurrentPic(String businessKey) {
				return R.error();
			}

			@Override
			public R getCoording(String businessKey) {
				return R.error();
			}
		};
	}
}
