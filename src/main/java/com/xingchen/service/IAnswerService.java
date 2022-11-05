package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Answer;
import com.xingchen.pojo.Note;
import com.xingchen.utils.R;

import java.util.List;
import java.util.Set;

public interface IAnswerService extends IService<Answer> {


    boolean updateAnswer(Answer answer);

    List<Answer> viewAnswerSelf(Integer topicId,Integer answerUid);


    boolean deleteMoreAnswer(Integer[] answerId);

    boolean deleteAnswer(Integer answerId);

    IPage<Answer> getUserAnswer(Integer answerUid, Integer currentPage, Integer pageSize);

    Long getSuccessCount(Integer topicId, Integer answerUid);

    Long getFailCount(Integer topicId, Integer answerUid);

    Answer viewAnswer(Integer topicId);


    List<Answer> viewTopicAnswer(Integer topicId);

    Set<Integer> getPassTopic(Integer answerUid);

    Set<Integer> getNotTopic(Integer answerUid);
}
