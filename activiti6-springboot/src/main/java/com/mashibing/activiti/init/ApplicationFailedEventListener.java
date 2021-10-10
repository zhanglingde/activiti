package com.mashibing.activiti.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-03-31 13:57
 */
@Component
@Slf4j
public class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        if (log.isInfoEnabled()) {
            log.error("Application started error!", event.getException());
        }
    }
}