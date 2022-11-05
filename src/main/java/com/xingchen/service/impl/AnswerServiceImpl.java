package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.AnswerMapper;
import com.xingchen.dao.TopicMapper;
import com.xingchen.pojo.Answer;
import com.xingchen.pojo.Comment;
import com.xingchen.pojo.Topic;
import com.xingchen.service.IAnswerService;
import com.xingchen.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {


    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private TopicMapper topicMapper;


    boolean flag=true;


    @Override
    @Transactional
    public boolean updateAnswer(Answer answer) {
        return answerMapper.updateById(answer)>0;
    }

    @Override
    public List<Answer> viewAnswerSelf(Integer topicId,Integer answerUid) {
        return answerMapper.viewAnswerSelf(topicId,answerUid);
    }

    @Override
    public boolean deleteMoreAnswer(Integer[] answerId) {
        return answerMapper.deleteBatchIds(Arrays.asList(answerId))>0;
    }

    @Override
    public boolean deleteAnswer(Integer answerId) {
        return answerMapper.deleteById(answerId)>0;
    }

    @Override
    public IPage<Answer> getUserAnswer(Integer answerUid, Integer currentPage, Integer pageSize) {
        QueryWrapper<Answer> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("answer_uid",answerUid);
        IPage<Answer> page=new Page<>(currentPage,pageSize);
        answerMapper.selectPage(page,courseQueryWrapper);
        return page;
    }

    @Override
    public Long getSuccessCount(Integer topicId, Integer answerUid) {
        QueryWrapper<Answer> answerQueryWrapper=new QueryWrapper<>();
        answerQueryWrapper.eq("answer_uid",answerUid);
        answerQueryWrapper.eq("topic_id",topicId);
        answerQueryWrapper.eq("answer_result",1);
        return answerMapper.selectCount(answerQueryWrapper);
    }

    @Override
    public Long getFailCount(Integer topicId, Integer answerUid) {
        QueryWrapper<Answer> answerQueryWrapper=new QueryWrapper<>();
        answerQueryWrapper.eq("answer_uid",answerUid);
        answerQueryWrapper.eq("topic_id",topicId);
        answerQueryWrapper.eq("answer_result",0);
        return answerMapper.selectCount(answerQueryWrapper);
    }

    @Override
    public Answer viewAnswer(Integer topicId) {
        return answerMapper.selectById(topicId);
    }

    @Override
    public List<Answer> viewTopicAnswer(Integer topicId) {
        List<Answer> answerList = answerMapper.viewTopicAnswer(topicId);
        return answerList;
    }

    @Override
    public Set<Integer> getPassTopic(Integer answerUid) {
        QueryWrapper<Answer> answerQueryWrapper=new QueryWrapper<>();
        answerQueryWrapper.eq("answer_uid",answerUid);
        List<Answer> answerList = answerMapper.selectList(answerQueryWrapper);
        Set<Integer> answerLists=new HashSet<>();
        for (Answer answer:answerList){
            Integer topicId = answer.getTopicId();
            answerLists.add(topicId);
        }
        if(answerLists.isEmpty()){
            flag=false;
        }
        return answerLists;
    }

    @Override
    public Set<Integer> getNotTopic(Integer answerUid) {
        List<Topic> topicList = topicMapper.selectList(null);
        Set<Integer> topicLists=new HashSet<>();
        for (Topic topic:topicList){
            Integer topicId = topic.getTopicId();
            topicLists.add(topicId);
        }
        System.out.println(topicLists);
        Set<Integer> passTopic = getPassTopic(answerUid);
        for(Integer integer:passTopic){
            if(topicLists.contains(integer)){
                topicLists.remove(integer);
            }
        }
        return topicLists;
    }

}
