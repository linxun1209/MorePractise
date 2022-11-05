package com.xingchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Post;
import com.xingchen.utils.Page;
import com.xingchen.utils.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * @author Li
 * @Date 2022/7/6 18:34
 */
public interface PostService extends IService<Post> {
    //存草稿
    boolean addDraft(Integer userId,String postTitle,String postContent,String postPicture);
    //上传图片
    String picture(MultipartFile file);
    //发布帖子
    R postPublish(Integer userId,String postTitle,String postContent,String postPicture,String postType,String postLabel,Integer postVip);
    //审核帖子
    void postAudit(Integer post,boolean audit);
    //设置仅自己可见(取消仅自己可见)
    R onlySelf(Integer postId);
    //删除帖子
    void postDelete(Integer postId);
    //批量删除帖子
    void postDeleteMore(Integer[] postId);
    //用户修改帖子信息
    void update(Integer postId,String postTitle,String postContent,String postPicture,String postType,String postLabel,Integer postVip);
    //查看单个帖子详细信息
    Post viewOne(Integer postId);
    //用户查看自己的帖子
    List<Post> viewSelf(Integer userId,Integer state,String begin,String end,String search);
    //查看所有发布成功的帖子
    Page<Post> postPublish(Integer currentPage, Integer pageSize);
    //分页查看所有帖子
    Page<Post> viewByPage(Integer currentPage, Integer pageSize,Integer state);
    //分页搜索帖子
    Page<Post> searchByPage(Integer currentPage,Integer pageSize,String search,Integer state);
    //收藏帖子(取消收藏)
    R collection(Integer userId,Integer postId);
    //判断是否收藏
    boolean isCollection(Integer userId,Integer postId);
    //查看用户收藏
    List<Post> viewCollection(Integer userId);
    //点赞帖子（取消点赞）
    R postLike(Integer userId,Integer postId);
    //判断是否点赞
    boolean postIsLike(Integer userId,Integer postId);
    //添加帖子类型
    String addType(String Type);
    //删除帖子类型
    void typeDelete(String type);
    //修改帖子类型
    R updateType(String oldType,String newType);
    //批量删除帖子类型
    void typeDeleteMore(String[] type);
    //查看所有类型
    Set<Object> typeView();
}
