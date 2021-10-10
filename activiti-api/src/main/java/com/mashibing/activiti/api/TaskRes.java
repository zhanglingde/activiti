package com.mashibing.activiti.api;

import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author:孙志强
 * @date 2020/09-24 10:00
 * @Modified BY:
 **/
@Data
public class TaskRes {

	private String processInstanceId;
	private String processDefinitionId;
	private String taskId;
	private String taskName;
	private String businessKey;
	private String assignee;
	private Date createTime;




}
