package com.mashibing.activiti.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashibing.activiti.api.R;
import com.mashibing.activiti.api.TaskRes;
import com.mashibing.activiti.service.ActivitiService;
import com.mashibing.activiti.utils.LoggerUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author:孙志强
 * @date 2020/09-08 15:59
 **/
@Service
public class ActivitiServiceImpl implements ActivitiService {

	@Resource
	private RepositoryService repositoryService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private TaskService taskService;
	@Resource
	private HistoryService historyService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Deployment deploymentProcessDefinition(String deploymentName,String categoryId, String bpmnFileName) {
		Deployment deploy = repositoryService.createDeployment()
				.name(deploymentName)
				.category(categoryId)
				.addClasspathResource("processes/" + bpmnFileName + ".bpmn")
				.addClasspathResource("processes/" + bpmnFileName + ".png")
				.deploy();
		return deploy;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteProcessDefinitionByKey(String processDefinitionKey, Boolean cascade) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).list();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition processDefinition : list) {
				String deploymentId = processDefinition.getDeploymentId();
				repositoryService.deleteDeployment(deploymentId,cascade);
			}
		}
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public InputStream viewPic(String categoryId) {
		String resourceName = "";
		List<Deployment> definitionList = repositoryService.createDeploymentQuery()
				.deploymentCategory(categoryId)
				.orderByDeploymenTime()
				.desc()
				.list();
		Deployment processDefinition;
		if(definitionList!=null && definitionList.size()>0){
			processDefinition = definitionList.get(0);
			List<String> list = repositoryService.getDeploymentResourceNames(processDefinition.getId());
			if (list != null && list.size() > 0) {
				for (String name : list) {
					if (name.indexOf(".png") >= 0) {
						resourceName = name;
					}
				}
			}
			InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getId(), resourceName);
			return inputStream;
		}else{
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R startProcess(String businessKey, String processDefinitionKey, String variables) {
		LoggerUtils.debug(ActivitiServiceImpl.class,"启动任务入参[businessKey:" + businessKey +  ",processDefinitionKey:" + processDefinitionKey + ",variables:"+variables+"]");
		try {
			// 1 幂等处理，判断此流程实例是否已经启动
			List<Task> listTask = getTasksByBusKey(businessKey);
			if (listTask != null && listTask.size() > 0) {
				LoggerUtils.debug(ActivitiServiceImpl.class, "业务数据：" + businessKey + "的任务已存在.");
				return R.error("该业务已存在审批中的数据");
			}
			// 2 json对象转Map，执行人信息在流程变量之中
			JSONObject  jsonObject = JSONObject.parseObject(variables);
			Map<String, Object> map = JSONObject.toJavaObject(jsonObject, Map.class);

			// 3 启动流程
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, map);

			//查询是否流程启动成功
			List<Task> tasks = getTasksByBusKey(businessKey);
			if (tasks == null || tasks.size() == 0) {
				return R.error("启动失败");
			}
			return R.ok().put("data", processInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("启动失败："+e.getMessage());
		}
	}



	@Override
	@Transactional(rollbackFor = Exception.class)
	public R completeTask(String businessKey, String variables) {
		LoggerUtils.debug(ActivitiServiceImpl.class,"提交任务入参[businessKey:" + businessKey + ",variables:" + variables + "]");
		// 1 json对象转Map，执行人信息在流程变量之中
		Map<String, Object> map = JSON.parseObject(variables, Map.class);
		if (CollectionUtils.isEmpty(map)) {
			return R.error("未指定下一个流程实例的办理人");
		}

		//只查询 ACT_RU_TASK 表中 Assignee 是 userid 的待办任务。
		List<Task> listTask = getTasksByAssignee(businessKey, (String) map.get("currentUser"));
		if (listTask==null || listTask.size()==0) {
			LoggerUtils.debug(ActivitiServiceImpl.class,"提交-未查询到：" + businessKey + "的任务");
			return R.error("提交的任务不存在");
		}
		if (listTask.size() != 1) {
			LoggerUtils.debug(ActivitiServiceImpl.class,"提交-业务主键：" + businessKey + "的任务不唯一");
			return R.error("该笔业务异常");
		}
		// 2 提交审核意见，审核意见放在variables中，key=common
		if(StringUtils.isNotBlank((String)map.get("common"))){
			taskService.addComment(listTask.get(0).getId(),listTask.get(0).getProcessInstanceId(),(String)map.get("common"));
		}
		//3 完成任务
		taskService.complete(listTask.get(0).getId(), map);
		List<Task> tasks = getTasksByBusKey(businessKey);

		//判断是否是最后一部
		String isFinished = "0";
		if(tasks == null || tasks.size()==0){
			isFinished = "1";
		}
		return R.ok("提交成功").put("isFinished",isFinished);
	}

	@Override
	public List<TaskRes> getTasksByUserId(String businessKey, String userId) {

		List<TaskRes> list;
		List<Task> tasks1 = getTasksByAssignee(businessKey, userId);
		List<Task> tasks2 = getTasksByCandidateUser(businessKey, userId);


		if(tasks1!=null && tasks1.size()>0){
			tasks1.addAll(tasks2);
			list = copyList(tasks1,userId);
		}else{
			list = copyList(tasks2,userId);
		}
		return list;
	}

	/**
	 * 功能描述: 根据业务主键查询流程连线集合<br>
	 *
	 * @param businessKey
	 * @Author 孙志强
	 * @date 2019/1/21 11:31
	 * @return:R
	 */
	@Override
	public R getOutComeListByBusinessKey(String businessKey) {
		List<String> list = new ArrayList<>();
		Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();

		String processDefinitionId = task.getProcessDefinitionId();
		//获取bpmnModel对象
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
		if (flowElement instanceof UserTask) {
			UserTask userTask = (UserTask) flowElement;
			System.out.println(userTask.getId());
			//获取连线信息
			List<SequenceFlow> incomingFlows = userTask.getOutgoingFlows();
			for (SequenceFlow sequenceFlow : incomingFlows) {
				if (sequenceFlow.getConditionExpression() != null && StringUtils.isNotBlank(sequenceFlow.getName())) {
					list.add(sequenceFlow.getName());
				}else{
					list.add("提交");
				}
			}
		}

		return R.ok().put("data",list);
	}

	private List<TaskRes> copyList(List<Task> taskList,String userId){
		List<TaskRes> list = new ArrayList<>();

		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				TaskRes taskRes = new TaskRes();
				taskRes.setAssignee(userId);
				taskRes.setCreateTime(task.getCreateTime());
				taskRes.setTaskId(task.getId());
				taskRes.setTaskName(task.getName());
				taskRes.setProcessDefinitionId(task.getProcessDefinitionId());
				taskRes.setProcessInstanceId(task.getProcessInstanceId());
				list.add(taskRes);
			}
		}
		return list;
	}


	@Override
	public R getCommonList(String businessKey) {
		List<Comment> taskCommentList = new ArrayList<>();
		String processInstanceId = "";
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey).orderByProcessInstanceStartTime().desc().list();
		processInstanceId = list.get(0).getId();

		taskCommentList.addAll(taskService.getProcessInstanceComments(processInstanceId));
		List<Map<String, Object>> taskComments = new ArrayList<>();
		if(taskCommentList != null && taskCommentList.size() > 0){
			for(Comment comment : taskCommentList){
				Map<String, Object> map = new HashMap<>(4);
				map.put("id", comment.getId());
				map.put("userId", comment.getUserId());
				map.put("fullMessage", comment.getFullMessage());
				map.put("time", comment.getTime());
				taskComments.add(map);
			}
		}
		return R.ok().put("data", taskComments);
	}

	@Override
	public R getHistoryTask(String userId) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(userId)
				.orderByTaskCreateTime()
				.desc()
				.list();
		return R.ok().put("data", list);
	}

	@Override
	public R deleteProcessInstance(String businessKey, String reason) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
		runtimeService.deleteProcessInstance(processInstance.getProcessInstanceId(), reason);
		return R.ok().put("data", "撤销流程成功！");
	}

	@Override
	public R getHistoryProcessInstance() {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
				.list();
		return R.ok().put("data", list);
	}

	@Override
	public InputStream viewCurrentPic(String businessKey) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.singleResult();
		HistoricProcessInstance historicProcessInstance = null;
		if(processInstance==null){
			historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
		}

		String processDefinitionId ;
		if(processInstance!=null){
			processDefinitionId = processInstance.getProcessDefinitionId();
		}else{
			processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(processDefinition.getDeploymentId()).singleResult();

		return viewPic(deployment.getCategory());
	}

	/** 签收任务 */
	private R signforTask(String businessKey, String userId) {
		LoggerUtils.debug(ActivitiServiceImpl.class,"签收任务入参[businessKey:" + businessKey + ",userId:" + userId + "]");
		List<Task> listTask = getTasksByCandidateUser(businessKey, userId);
		if (listTask== null || listTask.size()==0) {
			LoggerUtils.debug(ActivitiServiceImpl.class,"签收-未查询到：" + businessKey + "的任务");
			return R.error("您无权处理该笔数据");
		}
		if (listTask.size() != 1) {
			LoggerUtils.debug(ActivitiServiceImpl.class,"签收-业务主键：" + businessKey + "的任务不唯一");
			return R.error("该笔业务异常");
		}
		taskService.claim(listTask.get(0).getId(), userId);
		List<Task> tasks = getTasksByBusKey(businessKey);
		if (tasks== null || tasks.size()==0) {
			return R.error("签收失败");
		}
		return R.ok("签收成功");
	}
	/**
	 * Created with: 孙志强.
	 * Date: 2020/09/29  20:09
	 * Description: 根据 业务主键 + 签收人 获取任务实例，只查询待办列表中assignee有值的
	 */
	private List<Task> getTasksByAssignee(String businessKey, String userId) {
		List<Task> list = taskService.createTaskQuery()
				.processInstanceBusinessKey(businessKey)
				.taskAssignee(userId)
				.orderByTaskCreateTime()
				.asc()
				.list();
		return list;
	}

	/**
	 * Created with: 孙志强.
	 * Date: 2020/09/29  20:09
	 * Description: 根据 业务主键 + 签收人 获取组任务实例
	 */
	private List<Task> getTasksByCandidateUser(String businessKey, String userId) {
		return taskService.createTaskQuery()
				.processInstanceBusinessKey(businessKey)
				.taskCandidateUser(userId)
				.orderByTaskCreateTime()
				.asc()
				.list();
	}

	/**
	 * Created with: 孙志强.
	 * Date: 2020/09/29  20:09
	 * Description: 根据 业务主键 获取任务实例
	 */
	private List<Task> getTasksByBusKey(String businessKey) {
		List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
		return tasks;
	}

}
