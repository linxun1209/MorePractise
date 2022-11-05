package com.xingchen.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xingchen.dao.PostMapper;
import com.xingchen.pojo.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Li
 * @Date 2022/7/27 14:15
 */
@Component
@Slf4j
public class PostViewUtils {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PostMapper postMapper;

    @Scheduled(cron = "0 30 4 ? * * ")
    public void postViews() {
        System.out.println("执行");
        String pattern = "post@View:*";
        Set<String> keys = redisUtil.keys(pattern);
        for(String key:keys){
            String[] split = key.split(":");
            Integer postId=Integer.parseInt(split[1]);
            postMapper.updateView(postId,(Integer) redisUtil.get(key));
            redisUtil.del(key);
        }
    }
}
