package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.CommentMapper;
import com.xingchen.pojo.Comment;
import com.xingchen.service.ICommentService;
import com.xingchen.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper,Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;



    @Override
    public boolean deleteMoreComment(Integer[] commentId) {
        return commentMapper.deleteBatchIds(Arrays.asList(commentId))>0;
    }

    @Override
    public List<Comment> getComment(Integer topicId) {
        QueryWrapper<Comment> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("topic_id",topicId);
        return commentMapper.selectList(courseQueryWrapper);
    }

    @Override
    public List<Comment> getUserComment(Integer commenterId,Integer commentId) {
        QueryWrapper<Comment> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("commenter_id",commenterId);
        courseQueryWrapper.eq("comment_statue",commentId);
        return commentMapper.selectList(courseQueryWrapper);

    }



    @Override
    public boolean deleteFatherComment(Integer commenterId,Integer topicId,Integer commentId) {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("commenter_id",commenterId);
        commentQueryWrapper.eq("topic_id",topicId);
        commentQueryWrapper.eq("comment_id",commentId);
        commentQueryWrapper.eq("comment_statue",0).or().eq("comment_statue",commentId);
        return commentMapper.delete(commentQueryWrapper)>0;

    }

    @Override
    public boolean deleteSonComment(Integer commenterId,Integer topicId,Integer commentIdFather,Integer commentId,Integer commenterSonId) {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("commenter_id",commenterId);
        commentQueryWrapper.eq("topic_id",topicId);
        commentQueryWrapper.eq("comment_id",commentId);
        commentQueryWrapper.eq("comment_statue",commentIdFather);
        commentQueryWrapper.eq("commenter_son_id",commenterSonId);
        return commentMapper.delete(commentQueryWrapper)>0;
    }

    @Override
    public IPage<Comment> getCommentFather(Integer topicId,Integer currentPage,Integer pageSize) {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("topic_id",topicId);
        commentQueryWrapper.eq("comment_statue",0);
        IPage iPage=new Page(currentPage,pageSize);
        return commentMapper.selectPage(iPage,commentQueryWrapper);
    }

    @Override
    public boolean deleteComment(Integer commentId) {
        return commentMapper.deleteById(commentId)>0;
    }


    @Override
    public R isCommentSelf(Integer id, Integer commentId) {
        Comment comment1 = commentMapper.selectById(commentId);
        if (id == comment1.getCommenterId() && comment1.getCommentStatue() == 1) {
            return new R(false, "该用户是父评论");
        } else if (id == comment1.getCommenterId() && comment1.getCommentStatue() == 0) {
            return new R(false, "该用户是子评论");
        } else {
            return new R(false, "该用户不存在");
        }
    }


}
