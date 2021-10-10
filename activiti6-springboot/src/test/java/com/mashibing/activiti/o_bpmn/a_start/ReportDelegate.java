package com.mashibing.activiti.o_bpmn.a_start;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-09-08 22:20
 */
public class ReportDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("有人缺勤，上报");
    }
}
