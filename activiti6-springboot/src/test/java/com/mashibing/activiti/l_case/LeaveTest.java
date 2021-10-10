package com.mashibing.activiti.l_case;

import com.mashibing.activiti.ApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-07-27 22:33
 */
@Slf4j
public class LeaveTest extends ApplicationTests {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private ProcessEngineConfiguration processEngineConfiguration;


    private final String bpmnNameAndKey = "leave";

    /*  第一步：需求下来的时候，跟产品,跟业务，跟客户，一起确定的，研发绘制流程图？找客户，产品确认（需求确认）   */
    @Test
    public void deployment() {
        Deployment deployment = repositoryService.createDeployment().name("请假流程")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".bpmn")
                .addClasspathResource("processes/" + bpmnNameAndKey + ".png")
                .category("HR")
                .deploy();
        System.out.println("部署ID\t" + deployment.getId());
        System.out.println("部署分类\t" + deployment.getCategory());
        System.out.println("部署名称\t" + deployment.getName());
    }

    /* 第二步：启动流程：谁启动，发起人发起， 公司任何一个员工都可以*/
    @Test
    public void start() {
        Map<String, Object> variables = new HashMap<>();
        /* 发起人，来源于账号系统 */
        variables.put("inputUser", "张三");
        String bussnessKey = "leave-1";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey, bussnessKey, variables);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义KEY\t" + processInstance.getProcessDefinitionKey());
    }

    /* 第三步 ：代办列表中有自己的待办任务。消息通知（短信，邮件，企业erp系统）*/
    @Test
    public void findMyTask() {
        String assignee = "李四";
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();

        for (Task task : taskList) {
            System.out.println("任务ID\t" + task.getId());
            System.out.println("任务名称\t" + task.getName());
            System.out.println("流程定义ID\t" + task.getProcessDefinitionId());
            System.out.println("流程实例ID\t" + task.getProcessInstanceId());
            System.out.println("执行对象ID\t" + task.getExecutionId());
            System.out.println("任务创办时间\t" + task.getCreateTime());
            String businessKey = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult()
                    .getBusinessKey();
            /*  跟据businessKey查询自己业务系统表，查询出来请假单 */
            /*  返回来的是根据列表但详情 */
            // leave = leaveService.queryLeave(businessKey)
            System.out.println("=======");
        }
    }

    /* 第五步： 获取流程连线上面的变量-为了页面显示，为了页面传值，为了后台完成任务的时候，动态指定下一个任务节点 */
    @Test
    public void findOutLine() {
        String taskId = "7507";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

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
                if (sequenceFlow.getConditionExpression() != null) {
                    System.out.println("连线名称\t" + sequenceFlow.getName());
                    System.out.println("连线表达式\t" + sequenceFlow.getConditionExpression());
                    System.out.println("连线下一任务节点\t" + sequenceFlow.getTargetRef());
                }
            }
        }
        /**/
        GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowElement.getId());

        System.out.println("graphicInfo.getX() = " + graphicInfo.getX());
        System.out.println("graphicInfo.getY() = " + graphicInfo.getY());
        System.out.println("graphicInfo.getHeight() = " + graphicInfo.getHeight());
        System.out.println("graphicInfo.getWidth() = " + graphicInfo.getWidth());
    }

    /* 第四步：完成任务-指定下一个任务的办理人,添加评审意见--添加附件 */
    @Test
    public void compleTask() {
        String taskId = "2506";
        Map<String, Object> variables = new HashMap<>();
        /*  批准是页面用户操作按钮动态传递过来的，用来动态完成任务。 */
        variables.put("outcome", "批准");
        /* 指定下一个节点的办理人--办理人，通常都是自己的直属领导--此处，就要查询ERP系统，根据当前办理人查询 */
        variables.put("inputUser", "王五");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.addComment(taskId, task.getProcessInstanceId(), "部门经理同意");
        taskService.createAttachment("test", taskId, task.getProcessInstanceId(), "attachment name", "description", new ByteArrayInputStream("附件".getBytes()));
        taskService.complete(taskId,variables);

    }

    /* 获取评论意见 */
    @Test
    public void getComment() {
        String taskId = "5002";
        List<Comment> taskComments = taskService.getTaskComments(taskId);
        taskComments.forEach((taskComment)->{
            System.out.println(taskComment.getFullMessage());
        });

        System.out.println("*****************");
        String processInstanceId = "2501";
        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);

        processInstanceComments.forEach(item->{
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                    .taskId(item.getTaskId())
                    .singleResult();
            System.out.println(historicTaskInstance.getAssignee() + "\t" + historicTaskInstance.getName() + "\t" + item.getFullMessage());
        });
    }
    /* 获取附件信息 */
    @Test
    public void  getAttchment(){
        String taskId = "5002";
        List<Attachment> taskAttachments = taskService.getTaskAttachments(taskId);

        taskAttachments.forEach((attachment)->{
            System.out.println(attachment.getName());
            InputStream inputStream = taskService.getAttachmentContent(attachment.getId());

            StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(inputStream, writer);
                System.out.println(writer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("*****************");
        String processInstanceId = "2501";
        List<Attachment> processInstanceAttachments = taskService.getProcessInstanceAttachments(processInstanceId);
        taskAttachments.forEach((attachment)->{
            System.out.println(attachment.getName());
            InputStream inputStream = taskService.getAttachmentContent(attachment.getId());

            StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(inputStream, writer);
                System.out.println(writer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    /*  获取当前执行节点的流程图 */
    @Test
    public void getDiagram() throws Exception {
        String processInstanceId = "2501";
        //获得流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = StringUtils.EMPTY;
        if (processInstance == null) {
            //查询已经结束的流程实例
            HistoricProcessInstance processInstanceHistory =
                    historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId).singleResult();
            if (processInstanceHistory != null){
                processDefinitionId = processInstanceHistory.getProcessDefinitionId();
            }
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        //获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = Collections.EMPTY_LIST;
        if (processInstance != null)
            currentActs = runtimeService.getActiveActivityIds(processInstance.getId());
        InputStream inputStream = processEngineConfiguration
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, new ArrayList<String>(),
                        "宋体", "微软雅黑", "黑体", null, 2.0);

        FileUtils.copyInputStreamToFile(inputStream, new File("D://bbb.png"));
    }

    /* 撤回到第一个流程节点 ?  能不能动态跳转到任意节点?能-----*/
    @Test
    public void revoke() {
        String bussnessKey = "leave-1";
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(bussnessKey).singleResult();
        if(task==null) {
            System.out.println("流程未启动或已执行完成，无法撤回");
        }
        String userName = "张三";
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(bussnessKey)
                .orderByTaskCreateTime()
                .asc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        /* 循环历史流程节点，找到第一个流程 */
        for(HistoricTaskInstance hti : htiList) {
            /* 能扩展吧 */
            if(userName.equals(hti.getAssignee())) {
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if(null==myTaskId) {
            System.out.println("该任务非当前用户提交，无法撤回");
        }

        String processDefinitionId = myTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().list();
        for(HistoricActivityInstance hai : haiList) {
            if(myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        System.out.println("------->> activityId:" + activityId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

        //清理活动方向
        flowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(myFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);

        taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");

        Map<String,Object> currentVariables = new HashMap<>();
        currentVariables.put("inputUser", userName);
        //完成任务
        taskService.complete(task.getId(),currentVariables);
        task = taskService.createTaskQuery().processInstanceBusinessKey(bussnessKey).singleResult();
        taskService.setAssignee(task.getId(), userName);
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);
    }

    @Test
    public void delete(){
        String processInstanceId = "2501";
        runtimeService.deleteProcessInstance(processInstanceId, "测试删除");
    }
}
