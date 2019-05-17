package com.ch.common.config;

import com.alibaba.fastjson.JSONObject;
import com.ch.common.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Chen on 2019-02-16-14:39
 */
@ServerEndpoint(value = "/websocket/{sid}")
@Component
public class MyWebSocket {
    private static final Logger log = LoggerFactory.getLogger(MyWebSocket.class);
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private String sid = "";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        Message send = new Message(sid,"欢迎上线！","系统管理员");
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        //在线数加1
        addOnlineCount();
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        this.sid = sid;
        try {
            sendMessage(send);
        } catch (IOException e) {
            log.error("io error",e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端" + sid + "的消息:" + message);
        Message send = new Message(sid,message);
        //群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(send);
            } catch (IOException e) {
                log.error("",e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误",error);
    }

    /**
     * 服务端主动推送
     * @param message
     * @throws IOException
     */
    public void sendMessage(Message message) throws IOException {
        this.session.getBasicRemote().sendText(JSONObject.toJSONString(message));
    }

    public static void sendInfo(String message,@PathParam("sid") String sid,String fromUid) throws IOException {
        Message send = new Message(sid,message,fromUid);
        for (MyWebSocket item : webSocketSet) {
            if (null == sid) {
                item.sendMessage(send);
            } else if (item.sid.equals(sid)) {
                item.sendMessage(send);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}
