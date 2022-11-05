package com.xingchen.utils;

import com.alibaba.fastjson.JSONObject;
import com.xingchen.dao.MessageBodyMapper;
import com.xingchen.pojo.MessageBody;
import com.xingchen.utils.SocketStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.EOFException;
import java.io.IOException;
import java.util.Date;

/**
 * 服务器端逻辑实现
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.service.impl
 * @date 2022/7/20 15:53
 */
@Component
@ServerEndpoint("/socket/{name}")
@Slf4j
public class WebChatSocket {


    @Autowired
    private MessageBodyMapper messageBodyMapper;

    @OnOpen
    public void onOpen(@PathParam("name") String name, Session session) throws IOException {
        if (SocketStorage.sessionMap.putIfAbsent(name, session) != null){
            log.error("用户名：{} 已存在", name);
            throw new RuntimeException("用户名已存在，请更换用户名。");
        }
        SocketStorage.nameMap.put(session.getId(), name);
        log.info("{}上线了", name);
        MessageBody systemMessage = new MessageBody();
        systemMessage.setContent(name + "成功建立连接");
        systemMessage.setFromName("system");
        session.getBasicRemote().sendText(JSONObject.toJSONString(systemMessage));
    }

    @OnClose
    public void onClose(Session session){
        String name = SocketStorage.nameMap.remove(session.getId());
        SocketStorage.sessionMap.remove(name);
        log.info("{}下线了", name);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("监听到消息：{}", message);


        System.out.println(message);
        MessageBody messageBody = JSONObject.parseObject(message, MessageBody.class);
        log.info("...",messageBody);
        if (messageBody == null){
            log.warn("监听到的消息为空或格式不正确，message:{}", message);
            return;
        }
        String fromUser = SocketStorage.nameMap.get(session.getId());
        //设置发消息的人
        messageBody.setFromName(fromUser);
        messageBody.setSendTime(new Date());

        //将消息转发给收消息的人
        Session toSession = SocketStorage.sessionMap.get(messageBody.getToName());
        if (toSession != null){
            toSession.getBasicRemote().sendText(JSONObject.toJSONString(messageBody));

        }else {
            log.info("{}不在线，发送失败", messageBody.getToName());
            MessageBody systemMessage = new MessageBody();
            systemMessage.setContent(messageBody.getToName() + "不在线，发送失败");
            systemMessage.setFromName("system");
            session.getBasicRemote().sendText(JSONObject.toJSONString(systemMessage));
        }
    }

    @OnError
    public void onError(Session session, Throwable error){
        if (error instanceof EOFException && error.getMessage() == null){
            String name = SocketStorage.nameMap.get(session.getId());
            SocketStorage.nameMap.remove(session.getId());
            SocketStorage.sessionMap.remove(name);
            log.info("{}长时间没有活动，连接已断开", name);
        }
    }
}
