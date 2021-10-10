package com.mashibing.activiti.p_subprocess;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-09-08 22:20
 */
public class ErrorDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("错误事件");
        throw new BpmnError("myError");
    }
}
