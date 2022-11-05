package com.xingchen.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchen.pojo.Answer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerMapper extends BaseMapper<Answer> {


    List<Answer>  viewAnswerSelf(@Param("topic_id") Integer topicId,@Param("answer_uid") Integer answerUid);


    List<Answer> viewTopicAnswer(Integer topicId);
}
