package com.xingchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.PostCommentMapper;
import com.xingchen.dao.PostMapper;
import com.xingchen.dao.UserMapper;
import com.xingchen.pojo.Post;
import com.xingchen.pojo.PostComment;
import com.xingchen.service.PostCommentService;
import com.xingchen.utils.R;
import com.xingchen.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Li
 * @Date 2022/7/13 23:06
 */
@Service
public class PostCommentServiceImpl  extends ServiceImpl<PostCommentMapper, PostComment> implements PostCommentService {
    @Autowired
    private PostCommentMapper postCommentMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

    //添加评论
    @Override
    @Transactional
    public R addComment(Integer userId, Integer postId, String commentContent) {
        if(commentContent==null||commentContent.equals("")){
            return new R(false,"评论内容不能为空");
        }
        Date data=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String commentTime = simpleDateFormat.format(data);
        PostComment postComment=new PostComment(userId,null,postId,commentContent,commentTime,0);
        postCommentMapper.insert(postComment);
        userMapper.addNum(userId,10);
        return new R(true,"评论成功");
    }

    @Override
    @Transactional
    public R re(Integer userId, Integer commentId, String commentContent) {
        if(commentContent==null||commentContent.equals("")){
            return new R(false,"评论内容不能为空");
        }
        Date data=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String commentTime = simpleDateFormat.format(data);
        PostComment postComment=new PostComment(userId,null,commentId,commentContent,commentTime,1);
        postCommentMapper.insert(postComment);
        userMapper.addNum(userId,10);
        return new R(true,"评论成功");
    }

    //回复评论
//    @Override
//    @Transactional
//    public R response(Integer userId,Integer commentFather, Integer commentId, String commentContent) {
//        if(commentContent==null||commentContent.equals("")){
//            return new R(false,"评论内容不能为空");
//        }
//        Date data=new Date();
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String commentTime = simpleDateFormat.format(data);
//        PostComment postComment=new PostComment(userId,commentFather,commentId,commentContent,commentTime,1);
//        postCommentMapper.insert(postComment);
//        return new R(true,"评论成功");
//    }

    //判断是否可以删除评论
    @Override
    public boolean isDelete(Integer userId, Integer commentId,Integer postId) {
        PostComment postComment = postCommentMapper.selectById(commentId);
        Post post = postMapper.selectById(postId);
        if(userId==postComment.getUserId()||userId==post.getUserId())
            return true;
        return false;
    }

    //删除评论
    @Override
    @Transactional
    public void deleteComment(Integer commentId) {
        postCommentMapper.deleteById(commentId);
    }

    //查看一条帖子的所有评论
    @Override
    public List<PostComment> viewComment(Integer postId) {
        return postCommentMapper.viewComment(postId);
    }

    //点赞
    @Override
    @Transactional
    public R like(Integer commentId, Integer userId) {
        String likeKey="comment@Like:"+commentId;
        if(redisUtil.hasKey(likeKey)&&redisUtil.sHasKey(likeKey,userId)) {
            redisUtil.setRemove(likeKey,userId);
            return new R(true,"取消点赞");
        }
        redisUtil.sSet(likeKey,userId);
        return new R(true,"点赞");
    }

    //判断是否点赞
    @Override
    public boolean isLike(Integer commentId, Integer userId) {
        String likeKey="comment@Like:"+commentId;
        if(redisUtil.hasKey(likeKey)&&redisUtil.sHasKey(likeKey,userId))
            return true;
        return false;
    }

    @Override
    public Integer commentLikeNum(Integer commentId) {
        String likeKey="comment@Like:"+commentId;
        return (int)redisUtil.sGetSetSize(likeKey);
    }

}
