package com.xingchen.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Li
 * @Date 2022/7/27 16:03
 */
@Component
public class DeleteUtils {
    @Autowired
    private RedisUtil redisUtil;

    public void userDelete(Integer userId){
        Set<Object> objects = redisUtil.sGet("follow:" + userId);
        for(Object object:objects){
            redisUtil.setRemove("fan:"+object,userId);
        }
        objects = redisUtil.sGet("fan:" + userId);
        for(Object object:objects){
            redisUtil.setRemove("follow:"+object,userId);
        }
        redisUtil.del("collection@Post:"+userId);
        redisUtil.del("follow:"+userId);
        redisUtil.del("fan:"+userId);
    }

    public  void postDelete(Integer postId){
        Set<Object> objects = redisUtil.sGet("post@Collection:" + postId);
        for(Object object:objects){
            redisUtil.setRemove("collection@Post:"+object,postId);
        }
        redisUtil.del("post@Collection:"+postId);
        redisUtil.del("post@Like:" + postId);
        redisUtil.del("post@View:" + postId);
    }
}
