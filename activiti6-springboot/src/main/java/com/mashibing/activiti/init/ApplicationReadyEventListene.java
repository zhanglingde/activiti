package com.mashibing.activiti.init;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-03-31 13:56
 */
@Component
@Slf4j
public class ApplicationReadyEventListene implements ApplicationListener<ApplicationReadyEvent> {


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (log.isInfoEnabled()) {
            log.info("Application started successfully!");
        }
    }
}