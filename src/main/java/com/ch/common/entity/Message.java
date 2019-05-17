package com.ch.common.entity;

import lombok.Data;

/**
 * @author Chen on 2019-02-24-22:21
 */
@Data
public class Message {
    private String uid;
    private String message;
    private String fromUid;

    public Message(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public Message(String uid, String message, String fromUid) {
        this.uid = uid;
        this.message = message;
        this.fromUid = fromUid;
    }
}
