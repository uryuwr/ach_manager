package com.ch.common.Controller;

import com.ch.common.config.MyWebSocket;
import com.ch.common.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Chen on 2019-02-16-16:19
 */
@RestController
@RequestMapping("/v0.1/message")
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @GetMapping("/send/{sid}")
    public Message audit(@PathVariable String sid, @RequestParam String message,@RequestParam String fromUid) {
        try {
            //服务端发送消息给客户端
            MyWebSocket.sendInfo(message,sid,fromUid);
        } catch (IOException e) {
            //记录异常信息
            log.error("" + e.getMessage(),e);
        }
        return new Message(sid,message);
    }
}
