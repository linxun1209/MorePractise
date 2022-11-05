package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Topic;
import com.xingchen.pojo.User;
import com.xingchen.utils.R;

import java.util.List;


public interface ITopicService extends IService<Topic> {

    boolean modify(Topic topic);

    boolean delete(Integer topicId);

    IPage<Topic> getPage1(int currentPage, int pageSize);

    //批量删除对应的题
    boolean deleteMore(Integer[] topicId);

    IPage<Topic> getListUserId(Integer topicUid, Integer currentPage, Integer pageSize);

    //收藏题目
    R CollectionTopic(Integer topicId, Integer userId);
    //查看用户的题目
    List<Topic> UserCollectionTopic(Integer userId);

    IPage<Topic> getByWays(Topic topic,Integer currentPage, Integer pageSize);


    void topicAudit(Integer topicId, boolean audit);

    IPage<Topic> getAllTopic(Integer topicStatue, Integer currentPage, Integer pageSize);

    IPage<Topic> searchByPage(Integer currentPage, Integer pageSize, String search);

    IPage<Topic> searchByPageStatue(Integer currentPage, Integer pageSize, String search);

    IPage<Topic> searchByPageStatueOver(Integer currentPage, Integer pageSize, String search);


    boolean isCollectionTopic(Integer userId, Integer topicId);

    List<Topic> getAllPassTopic();

    List<Topic> getStatue(Integer topicStatue);

    List<Topic> getIsAudit(Integer topicUid, Integer topicStatue);
}
