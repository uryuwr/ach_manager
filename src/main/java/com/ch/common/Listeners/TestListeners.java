package com.ch.common.Listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Chen on 2019-02-16-12:41
 */
@Component
public class TestListeners {
    @EventListener
    public void processTestEvent(TestEvent event) {
        System.out.println("test event!");
    }
}
