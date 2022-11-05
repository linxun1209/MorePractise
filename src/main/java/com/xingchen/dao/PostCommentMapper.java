package com.xingchen.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchen.pojo.PostComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Li
 * @Date 2022/7/13 21:18
 */
@Repository
public interface PostCommentMapper extends BaseMapper<PostComment> {
    //查看某帖子的所有评论
    List<PostComment> viewComment(@Param("postId") Integer postId);
    //删除一个帖子的所有评论
    Integer postDelete(Integer postId);
    //删除一个用户的帖子的所有评论
    Integer userPostDelete(Integer userId);
    //删除一个用户的所有评论
    Integer userDelete(Integer userId);
}
