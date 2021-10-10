package com.mashibing.activiti.n_task.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * <p>java service实现具体类</p>
 *
 * @author 孙志强
 * @date 2020-09-19 16:32
 */
public class MyJavaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("脚本任务会自动执行");
        String var = (String) execution.getVariable("input");
        System.out.println(var);
        var = var.toUpperCase();
        execution.setVariable("input", var);
    }
}
