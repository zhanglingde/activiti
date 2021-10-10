package com.mashibing.activiti.service;

import com.mashibing.activiti.api.R;
import com.mashibing.activiti.api.TaskRes;
import org.activiti.engine.repository.Deployment;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-21 21:13
 */
public interface ActivitiService {

	/**
	 * 功能描述: 部署流程<br>
	 * @date 2019/1/9 14:20
	 * @param deploymentName  部署名称
	 * @param bpmnFileName 部署资源，当对于，resource/processes/文件夹
	 * @return Deployment 部署对象
	 */
	Deployment deploymentProcessDefinition(String deploymentName,String categoryId, String bpmnFileName);

	/**
	 * 功能描述: 删除流程定义<br>
	 * @date 2019/1/9 14:29
	 * @param processDefinitionKey  流程定义key
	 * @param cascade 是否级联
	 * @return:void
	 */
	void deleteProcessDefinitionByKey(String processDefinitionKey,Boolean cascade);
	/**
	 * 功能描述: 根据分类查询流程图<br>
	 * @date 2019/1/17 10:11
	 * @param categoryId
	 * @return:java.io.InputStream
	 */
	InputStream viewPic(String categoryId);

	/**
	 * 功能描述: 启动流程实例<br>
	 * @date 2019/1/17 10:25
	 * @param businessKey  业务主键
	 * @param processDefinitionKey  流程定义KEY
	 * @param variables  流程变量
	 * @return:org.activiti.engine.runtime.ProcessInstance
	 */
	R startProcess(String businessKey, String processDefinitionKey, String variables);

	/**
	 * 功能描述: 完成任务<br>
	 * @date 2019/1/17 11:03
	 * @param businessKey
	 * @param variables
	 * @return:void
	 */
	R completeTask(String businessKey, String variables);


	/**
	 * 功能描述: 查询个人代办任务<br>
	 * @date 2019/1/18 17:11
	 * @param businessKey
	 * @param userId
	 * @return:java.util.List<org.activiti.engine.task.Task>
	 */
	List<TaskRes> getTasksByUserId(String businessKey, String userId);

	/**
	 * 功能描述: 根据业务主键查询流程连线集合<br>
	 * @date 2019/1/21 11:31
	 * @param businessKey
	 * @return:R
	 */
	R getOutComeListByBusinessKey(String businessKey);

	/**
	 * 功能描述: 根据业务ID获取审核批注<br>
	 * @date 2019/1/23 9:46
	 * @param businessKey
	 * @return:R
	 */
	R getCommonList(String businessKey);

	/**
	 * 功能描述: 根据用户ID查询此用户历史任务<br>
	 * @date 2019/1/23 9:46
	 * @param userId
	 * @return:R
	 */
	R getHistoryTask(String userId);

	/**
	 * 功能描述: 查询历史流程实例<br>
	 * @date 2019/1/23 9:46
	 * @param 
	 * @return:R
	 */
	R getHistoryProcessInstance();

	/**
	 * 功能描述: 删除流程任务 记录在ACT_HI_PROCINST<br>
	 *
	 * @param businessKey
	 * @param reason
	 * @Author 孙志强
	 * @date 2019/3/3 08:51
	 * @return:R
	 */
	R deleteProcessInstance(String businessKey, String reason);

	/**
	 * 功能描述: 根据业务ID查询流程图<br>
	 * @date 2019/1/17 10:11
	 * @param businessKey
	 * @return:java.io.InputStream
	 */
	InputStream viewCurrentPic(String businessKey);


}


