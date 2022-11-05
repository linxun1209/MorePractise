package com.xingchen.utils;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.utils
 * @date 2022/7/20 15:51
 */
public class SocketStorage {

    private SocketStorage(){}

    /**
     * 根据name存储的Socket会话Map
     * key:name, value:session
     */
    public static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 根据Socket会话ID存储的name的Map
     * key:sessionId, value:name
     */
    public static ConcurrentHashMap<String, String> nameMap = new ConcurrentHashMap<>();
}
