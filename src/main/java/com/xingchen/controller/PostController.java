package com.xingchen.controller;

import com.xingchen.pojo.Post;
import com.xingchen.service.PostService;
import com.xingchen.service.impl.PostServiceImpl;
import com.xingchen.utils.Page;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * @author Li
 * @date 2022/7/6 18:53
 */
@Controller
@CrossOrigin
@Api(tags = "帖子")
public class PostController {
    @Autowired
    private PostService postService;

    /**
     *存草稿
     * @date 2022/7/6 18:56
     * @param userId 用户id
     * @param postTitle 帖子标题
     * @param postContent 帖子内容
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "保存草稿")
    @PostMapping(value = "/user/addDraft")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postTitle",value = "帖子标题"),
            @ApiImplicitParam(name = "postContent",value = "帖子内容")
    })
    @ResponseBody
    public R addDraft(Integer userId,String postTitle,String postContent,String postPicture){
        postService.addDraft(userId,postTitle,postContent,postPicture);
        return new R(true,"保存成功");
    }

    /**
     *上传图片
     * @date 2022/7/8 16:01
     * @param file 上传的文件
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "上传图片")
    @PostMapping(value = "/picture")
    @ResponseBody
    public R picture(MultipartFile file){
        String picture = postService.picture(file);
        return new R(true,picture);
    }

    /**
     *发布帖子
     * @date 2022/7/13 14:08
     * @param userId 用户id
     * @param postTitle 帖子标题
     * @param postContent 帖子内容
     * @param postPicture 帖子图片
     * @param postType 帖子类型
     * @param postLabel 帖子标签
     * @param postVip 是否需要vip
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "发布帖子")
    @PostMapping(value = "/user/postPublish")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postType",value = "帖子类型"),
            @ApiImplicitParam(name = "postLabel",value = "帖子标签")
    })
    @ResponseBody
    public R postPublish(Integer userId,String postTitle,String postContent,String postPicture,String postType,String postLabel,Integer postVip){
        return postService.postPublish(userId,postTitle,postContent,postPicture,postType,postLabel,postVip);
    }

    /**
     *审核帖子
     * @date 2022/7/6 19:47
     * @param postId 帖子id
     * @param audit 是否通过
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "审核帖子")
    @PostMapping(value = "/administrator/postAudit")
    @ApiImplicitParam(name = "audit",value = "审核结果，true为通过审核，false为未通过",required = true)
    @ResponseBody
    public R postAudit(Integer postId,boolean audit){
        postService.postAudit(postId,audit);
        return new R(true,"审核成功");
    }

    /**
     *设置成仅自己可见
     * @date 2022/7/17 16:43
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "设置成仅自己可见")
    @PutMapping(value = "/user/postOnlySelf")
    @ResponseBody
    public R postOnlySelf(Integer postId){
        return postService.onlySelf(postId);
    }

    /**
     *删除帖子
     * @date 2022/7/6 19:23
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "删除帖子")
    @DeleteMapping(value = "/all/postDelete")
    @ResponseBody
    public R postDelete(Integer postId){
        postService.postDelete(postId);
        return new R(true,"删除成功");
    }

    /**
     *批量删除帖子
     * @date 2022/7/6 19:25
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "批量删除帖子")
    @DeleteMapping(value = "/all/postDeleteMore")
    @ResponseBody
    public R postDeleteMore(Integer[] postId){
        postService.postDeleteMore(postId);
        return new R(true,"删除成功");
    }


    /**
     *修改帖子信息
     * @date 2022/7/9 18:18
     * @param postId 帖子id
     * @param postTitle 帖子标题
     * @param postContent 帖子内容
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "修改帖子信息")
    @PutMapping(value = "/user/postUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postTitle",value = "帖子标题"),
            @ApiImplicitParam(name = "postContent",value = "帖子内容")
    })
    @ResponseBody
    public R postUpdate(Integer postId,String postTitle,String postContent,String postPicture,String postType,String postLabel,Integer postVip){
        postService.update(postId,postTitle,postContent,postPicture,postType,postLabel,postVip);
        return new R(true,"修改成功");
    }

    /**
     *查看用户自己的帖子
     * @date 2022/7/6 20:43
     * @param userId 用户id
     * @return com.xingchen.utils.R
     */
    @ApiOperation(value = "查看用户自己的帖子")
    @GetMapping(value = "/user/viewSelf")
    @ResponseBody
    public R viewSelf(Integer userId,Integer state,String begin,String end,String search){
        List<Post> posts = postService.viewSelf(userId,state,begin,end,search);
        return new R(true,posts);
    }

    /**
     *查看审核通过的帖子
     * @date 2022/7/13 20:29
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看所有发布成功的帖子")
    @GetMapping(value = "/postPublic")
    @ResponseBody
    public R postPublic(Integer currentPage, Integer pageSize){
        Page<Post> posts = postService.postPublish(currentPage,pageSize);
        if(posts==null)
            return new R(false,"页数不合规范");
        return new R(true,posts);
    }

    /**
     *查看单个帖子详细信息
     * @date 2022/7/9 18:20
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看单个帖子详细信息")
    @GetMapping(value = "/postUpdate")
    @ResponseBody
    public R postViewOne(Integer postId){
        Post post = postService.viewOne(postId);
//        if(post==null){
//            return new R(false,"该用户不是vip");
//        }
        return new R(true,post);
    }

    /**
     *分页查看所有帖子
     * @date 2022/7/9 18:38
     * @param currentPage 当前页数
     * @param pageSize 每页个数
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "分页查看所有帖子")
    @GetMapping(value = "/administrator/postViewPage")
    @ApiImplicitParam(name="state",value = "null为全部，1为未审核，2为已经审核" +
            "")
    @ResponseBody
    public R postViewPage(Integer currentPage, Integer pageSize,Integer state){
        Page<Post> postPage = postService.viewByPage(currentPage, pageSize,state);
        if(postPage==null)
            return new R(false,"页数不合规范");
        return new R(true,postPage);
    }

    /**
     *分页搜索所有帖子
     * @date 2022/7/9 18:40
     * @param search 搜索内容
     * @param currentPage 当前页数
     * @param pageSize 每页个数
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "分页搜索所有帖子")
    @GetMapping(value = "/postSearchPage")
    @ApiImplicitParam(name="state",value = "null为全部，1为未审核，2为已审核")
    @ResponseBody
    public R postSearchPage(String search,Integer currentPage, Integer pageSize,Integer state){
        Page<Post> postPage = postService.searchByPage(currentPage, pageSize,search,state);
        if(postPage==null)
            return new R(false,"页数不合规范");
        return new R(true,postPage);
    }

    /**
     *收藏帖子（取消收藏）
     * @date 2022/7/12 15:08
     * @param userId 用户id
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "收藏帖子（取消收藏）")
    @GetMapping(value = "/user/postCollection")
    @ResponseBody
    public R collection(Integer userId,Integer postId){
        return postService.collection(userId, postId);
    }

    /**
     *判断是否收藏
     * @date 2022/7/12 15:12
     * @param userId 用户id
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "判断是否收藏")
    @GetMapping(value = "/user/postIsCollection")
    @ResponseBody
    public R isCollection(Integer userId,Integer postId){
        boolean is = postService.isCollection(userId, postId);
        if(is)
            return new R(true,"已收藏");
        return new R(true,"未收藏");
    }

    /**
     *查看用户收藏列表
     * @date 2022/7/12 15:14
     * @param userId 用户id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看用户收藏列表")
    @GetMapping(value = "/user/viewPostCollection")
    @ResponseBody
    public R viewCollection(Integer userId){
        List<Post> postList = postService.viewCollection(userId);
        return new R(true,postList);
    }

    /**
     *点赞帖子，取消点赞
     * @date 2022/7/12 17:28
     * @param userId 用户id
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "点赞帖子（取消点赞）")
    @GetMapping(value = "/user/postLike")
    @ResponseBody
    public R like(Integer userId,Integer postId){
        return postService.postLike(userId, postId);
    }

    /**
     *判断是否收藏
     * @date 2022/7/12 17:29
     * @param userId 用户id
     * @param postId 帖子id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "判断是否收藏")
    @GetMapping(value = "/user/postIsLike")
    @ResponseBody
    public R isLike(Integer userId,Integer postId){
        boolean is = postService.postIsLike(userId, postId);
        if(is)
            return new R(true,"已点赞");
        return new R(true,"未点赞");
    }

    /**
     *添加帖子类型
     * @date 2022/7/9 18:43
     * @param type 帖子类型
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "添加帖子类型")
    @PostMapping(value = "/administrator/postTypeAdd")
    @ResponseBody
    public R postTypeAdd(String type){
        String result = postService.addType(type);
        if(result==null)
            return new R(true,"添加成功");
        return new R(false,result);
    }

    /**
     *修改帖子类型
     * @date 2022/7/17 16:02
     * @param oldType 旧帖子类型
     * @param newType 新帖子类型
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "修改帖子类型")
    @PutMapping(value = "/administrator/postTypeUpdate")
    @ResponseBody
    public R postTypeUpdate(String oldType,String newType){
        return postService.updateType(oldType,newType);
    }

    /**
     *删除帖子类型
     * @date 2022/7/9 18:45
     * @param type 帖子类型
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "删除帖子类型")
    @DeleteMapping(value = "/administrator/postTypeDelete")
    @ResponseBody
    public R postTypeDelete(String type){
        postService.typeDelete(type);
        return new R(true,"删除成功");
    }

    /**
     *批量删除帖子类型
     * @date 2022/7/9 18:46
     * @param type 帖子类型数组
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "批量删除帖子类型")
    @DeleteMapping(value = "/administrator/postTypeDeleteMore")
    @ResponseBody
    public R postTypeDeleteMore(String[] type){
        postService.typeDeleteMore(type);
        return new R(true,"删除成功");
    }

    /**
     *查看所有帖子类型
     * @date 2022/7/12 17:26
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看所有帖子类型")
    @GetMapping(value = "/all/postTypeView")
    @ResponseBody
    public R postTypeView(){
        Set<Object> objects = postService.typeView();
        return new R(true,objects);
    }

}
