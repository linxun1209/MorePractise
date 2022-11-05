package com.xingchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.PostComment;
import com.xingchen.utils.R;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author Li
 * @Date 2022/7/13 23:06
 */
public interface PostCommentService extends IService<PostComment> {
    //发布评论
    R addComment(Integer userId,Integer postId,String commentContent);
    //回复评论
    R re(Integer userId,Integer commentId,String commentContent);
//    R response(Integer userId,Integer commentFather,Integer commentId,String commentContent);
    //判断是否是自己的评论
    boolean isDelete(Integer userId, Integer commentId, Integer postId);
    //删除评论
    void deleteComment(Integer commentId);
    //查看帖子所有评论
    List<PostComment> viewComment(Integer postId);
    //点赞评论
    R like(Integer commentId,Integer userId);
    //判断是否点赞
    boolean isLike(Integer commentId,Integer userId);
    //获取评论点赞数
    Integer commentLikeNum(Integer commentId);
}
