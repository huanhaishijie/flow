package com.sophony.flow.worker.common;

import org.springframework.context.ApplicationEvent;

/**
 * @author : yzm
 * @date : 2022-03-11 14:07
 * description:<p></p>
 **/

public abstract class BaseEvent<T> extends ApplicationEvent {

    String state = "FAIL";

    public BaseEvent(T source) {
        super(source);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
