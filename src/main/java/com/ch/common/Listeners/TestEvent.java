package com.ch.common.Listeners;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author Chen on 2019-02-16-12:41
 */
@Data
public class TestEvent extends ApplicationEvent {
    private String address;

    public TestEvent(Object source,String address) {
        super(source);
        this.address = address;
    }

}
