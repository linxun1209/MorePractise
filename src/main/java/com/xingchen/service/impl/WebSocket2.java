package com.xingchen.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.service.impl
 * @date 2022/7/25 22:17
 */
@ServerEndpoint(value ="/WebSocket2")
@Component
@Slf4j
public class WebSocket2 {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocket2> webSocketSet = new CopyOnWriteArraySet<WebSocket2>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;   //加入set中
        webSocketSet.add(this);
        addOnlineCount();  //在线数加1
//        System.out.println("有新连接加入！当前在线人数为：" + getOnlineCount());
        log.info("有新连接加入！当前在线人数为：" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.info("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
//        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        System.out.println("来自客户端的消息:" + message);
        log.info("来自客户端的消息:" + message);
        //群发消息
        for (WebSocket2 item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     @OnError
     */
    public void onError(Session session, Throwable error) {
//        System.out.println("发生错误");
        log.info("发生错误");
        error.printStackTrace();
    }

    /*
     * @Title: sendMessage
     * @Description : 发送消息
     * @param : message
     * @Return : void
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /*
     * @Title: sendInfo
     * @Description : 群发自定义消息
     * @param : message
     * @Return : void
     */
    public  void sendInfo(String message) throws IOException {
        for (WebSocket2 item : webSocketSet) {
            try {
                item.sendMessage(message);

            } catch (IOException e) {
                continue;
            }
        }
    }

    /*
     * @Title: getOnlineCount
     * @Description : 获得当前在线人数
     * @param :
     * @Return : int
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /*
     * @Title: addOnlineCount
     * @Description : 在线人数+1
     * @param :
     * @Return : void
     */
    public static synchronized void addOnlineCount() {
        WebSocket2.onlineCount++;
    }

    /**
     * @Title: subOnlineCount
     * @Description :在线人数-1
     * @param :
     * @Return : void
     */
    public static synchronized void subOnlineCount() {
        WebSocket2.onlineCount--;
    }

}