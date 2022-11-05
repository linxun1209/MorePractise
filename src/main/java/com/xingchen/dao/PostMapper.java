package com.xingchen.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchen.pojo.Post;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Li
 * @Date 2022/7/6 18:18
 */
@Repository
public interface PostMapper  extends BaseMapper<Post>{
    //查看单个帖子详细信息
    Post viewOne(Integer postId);
    //更新浏览量
    Integer updateView(@Param("postId") Integer postId,@Param("view") Integer view);
    //根据帖子id查找帖子
    List<Post> viewById(@Param("postId") List<Integer> postId);
    //用户查看自己的帖子
    List<Post> viewSelf(@Param("userId") Integer userId,@Param("state") Integer state,@Param("begin") String begin,@Param("end") String end,@Param("search") String search);
    //查看发布成功的帖子
    List<Post> viewPublish(@Param("begin") Integer begin,@Param("num") Integer num);
    //分页查看帖子
    List<Post> page(@Param("search") String search,@Param("begin") Integer begin,@Param("num") Integer num,@Param("state") Integer state);
}
