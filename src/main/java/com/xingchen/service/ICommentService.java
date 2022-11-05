package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Comment;
import com.xingchen.utils.R;

import java.util.List;

public interface ICommentService extends IService<Comment> {


    boolean deleteMoreComment(Integer[] commentId);


    List<Comment> getComment(Integer topicId);

    List<Comment> getUserComment(Integer commenterId,Integer commentId);

    R isCommentSelf(Integer id, Integer commentId);

    boolean deleteFatherComment(Integer commenterId,Integer topicId,Integer commentId);


    boolean deleteSonComment(Integer commenterId,Integer topicId,Integer commentIdFather,Integer commentId, Integer commenterSonId);

    IPage<Comment> getCommentFather(Integer topicId,Integer currentPage,Integer pageSize);

    boolean deleteComment(Integer commentId);

}
