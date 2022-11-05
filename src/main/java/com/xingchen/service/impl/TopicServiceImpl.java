package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.CommentMapper;
import com.xingchen.dao.TopicMapper;
import com.xingchen.pojo.*;
import com.xingchen.service.ITopicService;

import com.xingchen.utils.R;
import com.xingchen.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements ITopicService {
    @Autowired
    private TopicMapper topicDao;
    @Autowired
    private CommentMapper collectionMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public boolean modify(Topic topic) {
        return topicDao.updateById(topic)>0;
    }

    @Override
    @Transactional
    public boolean delete(Integer topicId) {
        return topicDao.deleteById(topicId)>0;
    }

    @Override
    @Transactional
    public boolean deleteMore(Integer[] topicId) {
        return topicDao.deleteBatchIds(Arrays.asList(topicId))>0;
    }

    @Override
    public IPage<Topic> getListUserId(Integer topicUid, Integer currentPage, Integer pageSize) {
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_uid",topicUid);
        IPage<Topic> page=new Page<>(currentPage,pageSize);
        IPage<Topic> topicIPage = topicDao.selectPage(page, queryWrapper);
        return topicIPage;
    }


    @Override
    public R CollectionTopic(Integer topicId, Integer userId) {
        String collectionKey="collection:"+topicId;
        String userIdKey="userIdKey:"+userId;
        String msg="";
        if(redisUtil.hasKey(collectionKey)&&redisUtil.sHasKey(collectionKey,userId)){
            redisUtil.setRemove(collectionKey,userId);
            redisUtil.setRemove(userIdKey,topicId);
            msg="取消收藏";
        }else{
            redisUtil.sSet(collectionKey,userId);
            redisUtil.sSet(userIdKey,topicId);

            msg="成功收藏";
        }
        return new R(true,msg);
    }



    @Override
    public boolean isCollectionTopic(Integer userId, Integer topicId) {
        String collectionKey="collection:"+topicId;
        if(!redisUtil.hasKey(collectionKey)){
            return  false;
        }
        return redisUtil.sHasKey(collectionKey,userId);
    }

    @Override
    public List<Topic> getAllPassTopic() {
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_statue",1);
        List<Topic> topicList = topicDao.selectList(queryWrapper);
        return topicList;
    }

    @Override
    public List<Topic> getStatue(Integer topicStatue) {
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_statue",topicStatue);
        List<Topic> topicList = topicDao.selectList(queryWrapper);
        return topicList;
    }

    @Override
    public List<Topic> getIsAudit(Integer topicUid, Integer topicStatue) {
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_statue",topicStatue);
        queryWrapper.eq("topic_uid",topicUid);
        List<Topic> topicList = topicDao.selectList(queryWrapper);
        return topicList;
    }


    //查看用户收藏的题目
    @Override
    public List<Topic> UserCollectionTopic(Integer userId) {
        String userIdKey="userIdKey:"+userId;
        if(!redisUtil.hasKey(userIdKey)){
            return null;
        }
        Set<Object> collection=redisUtil.sGet(userIdKey);
        List<Integer> id=new ArrayList(collection);
        if(id.isEmpty()){

            return null;
        }
        return topicDao.selectBatchIds(id);
    }

    @Override
    public IPage<Topic> getByWays(Topic topic,Integer currentPage, Integer pageSize) {
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(topic.getTopicProfessional())){
            queryWrapper.eq("topic_professional",topic.getTopicProfessional());
        }
        if(!StringUtils.isEmpty(topic.getTopicType())){
            queryWrapper.eq("topic_type",topic.getTopicType());
        }
        if(!StringUtils.isEmpty(topic.getTopicCourse())){
            queryWrapper.eq("topic_course",topic.getTopicCourse());
        }
        if(!StringUtils.isEmpty(topic.getTopicDifficulty())){
            queryWrapper.eq("topic_difficulty",topic.getTopicDifficulty());
        }
        if(!StringUtils.isEmpty(topic.getTopicVip())){
            queryWrapper.eq("topic_vip",topic.getTopicVip());
        }
        queryWrapper.eq("topic_statue",1);
        IPage page=new Page(currentPage,pageSize);
        topicDao.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public void topicAudit(Integer topicId, boolean audit) {
        Topic topic = topicDao.selectById(topicId);
        if(!audit){
            topic.setTopicStatue(-1);
        }
        else {
            topic.setTopicStatue(1);
        }
        topicDao.updateById(topic);
    }

    @Override
    public IPage<Topic> getAllTopic(Integer topicStatue, Integer currentPage, Integer pageSize) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        if(topicStatue==0){
            queryWrapper.eq("topic_statue",0);
        }else if(topicStatue==-1||topicStatue==1) {
            queryWrapper.eq("topic_statue", 1).or().eq("topic_statue", -1);
        }
        IPage<Topic> page=new Page<>(currentPage,pageSize);
        IPage<Topic> topicIPage = topicDao.selectPage(page, queryWrapper);
        return topicIPage;
    }

    @Override
    public IPage<Topic> searchByPage(Integer currentPage, Integer pageSize, String search) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_statue",1).like("topic_name",search);
//        queryWrapper.like("topic_type",search).or().like("topic_name",search);
//        queryWrapper.like("topic_name",search).or().like("topic_content",search).or().like("topic_type",search).or().like("topic_difficulty",search);


        IPage<Topic> page=new Page<>(currentPage,pageSize);
        topicDao.selectPage(page, queryWrapper);
        System.out.println(page);
        return page;
    }

    @Override
    public IPage<Topic> searchByPageStatue(Integer currentPage, Integer pageSize, String search) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_statue",0);
        queryWrapper.like("topic_name",search);
//        queryWrapper.like("topic_name",search).or().like("topic_content",search).or().like("topic_type",search).or().like("topic_difficulty",search).or().like("topic_course",search).or().like("topic_professional",search);
        IPage<Topic> page=new Page<>(currentPage,pageSize);
        topicDao.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public IPage<Topic> searchByPageStatueOver(Integer currentPage, Integer pageSize, String search) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("topic_statue",1);
//        queryWrapper.eq("topic_statue", 1).or().eq("topic_statue", -1);
        queryWrapper.like("topic_name",search);
//        queryWrapper.like("topic_name",search).or().like("topic_content",search).or().like("topic_type",search).or().like("topic_difficulty",search).or().like("topic_course",search).or().like("topic_professional",search);
        IPage<Topic> page=new Page<>(currentPage,pageSize);
        topicDao.selectPage(page, queryWrapper);
        return page;
    }




    @Override
    @Transactional
    public IPage<Topic> getPage1(int currentPage, int pageSize) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("topic_statue",1);
        IPage page=new Page(currentPage,pageSize);
        topicDao.selectPage(page,queryWrapper);
        return page;
    }

}
