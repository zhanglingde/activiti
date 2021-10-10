package com.mashibing.activiti.controller;

import com.mashibing.activiti.api.R;
import com.mashibing.activiti.service.ActivitiService;
import com.mashibing.activiti.utils.Base64Convert;
import com.mashibing.activiti.utils.LoggerUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author:孙志强
 * @date 2020/09-08 15:59
 */
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;

    /**
     * Created with: jingyan.
     * Date: 2016/10/17  11:46
     * Description: 手动部署工作流
     */
    @RequestMapping(value = "/deploymentProcessDefinition/{deploymentName}/{categoryId}/{bpmnFileName}")
    @HystrixCommand
    public R deploymentProcessDefinition(@PathVariable("deploymentName") String deploymentName, @PathVariable("categoryId") String categoryId, @PathVariable("bpmnFileName") String bpmnFileName) {
        R error = R.error("部署失败");
        try {
//            TimeUnit.SECONDS.sleep(8);
            Deployment deployment = activitiService.deploymentProcessDefinition(deploymentName, categoryId, bpmnFileName);
            return R.ok().put("data", deployment);
        } catch (Exception e) {
            e.printStackTrace();
            error.put("data", e.getMessage());
        }
        return error;
    }


    @RequestMapping(value = "/deleteProcessDefinitionByKey/{processDefinitionKey}/{cascade}")
    @HystrixCommand
    public R deleteProcessDefinitionByKey(@PathVariable("processDefinitionKey") String processDefinitionKey, @PathVariable("cascade") Boolean cascade) {
        try {
            activitiService.deleteProcessDefinitionByKey(processDefinitionKey, cascade);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/viewPic/{categoryId}")
    @HystrixCommand
    public R viewPic(@PathVariable("categoryId") String categoryId) throws Exception {
        InputStream inputStream = activitiService.viewPic(categoryId);
        if (inputStream != null) {
            String base64 = Base64Convert.ioToBase64(inputStream);
            LoggerUtils.debug(ActivitiController.class, "查找成功");
            return R.ok().put("data", base64);
        } else {
            LoggerUtils.error(ActivitiController.class, "没查询到流程图");
            return R.error("不存在流程图");
        }
    }

    /**
     * Created with: 孙志强.
     * Date: 2016/10/17  11:47
     * Description: 启动工作流
     */
    @RequestMapping("/startProcess/{businessKey}/{processDefinitionKey}/{variables}")
    @HystrixCommand
    public R startProcess(@PathVariable("businessKey") String businessKey,  @PathVariable("processDefinitionKey") String processDefinitionKey, @PathVariable("variables") String variables) {
        try {
            return activitiService.startProcess(businessKey, processDefinitionKey, variables);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("启动任务失败");
    }

    @RequestMapping("/getTasksByUserId/{userId}")
    @HystrixCommand
    public R getTasksByUserId(@PathVariable("userId") String userId) {
        try {
            return R.ok().put("data", activitiService.getTasksByUserId(null, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("查询任务失败");
    }

    /**
     * Created with: 孙志强.
     * Date: 2016/10/17  16:26
     * Description: 完成任务
     */
    @RequestMapping("/completeTask/{businessKey}/{variables}")
    @HystrixCommand
    public R completeTask(@PathVariable("businessKey") String businessKey,  @PathVariable("variables") String variables) {
        try {
            return activitiService.completeTask(businessKey, variables);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("提交任务失败");
    }

    /**
     * Created with: 孙志强.
     * Date: 2016/10/17  16:26
     * Description: 获取流程连线名称
     */
    @RequestMapping("/getOutComeListByBusinessKey/{businessKey}")
    @HystrixCommand
    public R getOutComeListByBusinessKey(@PathVariable("businessKey") String businessKey) {
        try {
            return activitiService.getOutComeListByBusinessKey(businessKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("获取失败");
    }

    /**
     * Created with: 孙志强.
     * Date: 2016/10/17  16:26
     * Description: 获取审核意见
     */
    @RequestMapping("/getCommonList/{businessKey}/{userId}")
    @HystrixCommand
    public R getCommonList(@PathVariable("businessKey") String businessKey, @PathVariable("userId") String userId) {
        try {
            return activitiService.getCommonList(businessKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("获取失败");
    }

    /**
     * Created with: 孙志强.
     * Date: 2016/10/17  16:26
     * Description: 获取历史任务
     */
    @RequestMapping("/getHistoryTask/{userId}")
    @HystrixCommand
    public R getHistoryTask(@PathVariable("userId") String userId) {
        try {
            return activitiService.getHistoryTask(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("获取历史任务失败");
    }

    /**
     * Created with: 孙志强.
     * Date: 2016/10/17  16:26
     * Description: 获取历史流程实例
     */
    @RequestMapping("/getHistoryProcessInstance")
    @HystrixCommand
    public R getHistoryProcessInstance() {
        try {
            return activitiService.getHistoryProcessInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("获取历史流程实例失败");
    }

    /**
     * Created with: 孙志强
     * Date: 2019/4/4  08:59
     * Description: 删除流程任务
     */
    @RequestMapping("/deleteProcessInstance/{businessKey}/{reason}")
    @HystrixCommand
    public R deleteProcessInstance(@PathVariable("businessKey") String businessKey, @PathVariable("reason") String reason) {
        try {
            return activitiService.deleteProcessInstance(businessKey, reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("撤销流程任务失败");
    }

    /**
     * Created with: 孙志强.
     * Date: 2016/10/17  16:26
     * Description: 获取历史流程实例
     */
    @RequestMapping("/viewCurrentPic/{businessKey}")
    @HystrixCommand
    public R viewCurrentPic(@PathVariable("businessKey") String businessKey) {
        InputStream inputStream = activitiService.viewCurrentPic(businessKey);
        if (inputStream != null) {
            String base64 = null;
            try {
                base64 = Base64Convert.ioToBase64(inputStream);
                LoggerUtils.debug(ActivitiController.class, "查找成功");
                return R.ok().put("data", base64);
            } catch (IOException e) {
                e.printStackTrace();
                return R.error("不存在流程图");
            }
        } else {
            LoggerUtils.error(ActivitiController.class, "没查询到流程图");
            return R.error("不存在流程图");
        }
    }

}
