package com.xingchen.controller;

import com.xingchen.pojo.PostComment;
import com.xingchen.service.PostCommentService;
import com.xingchen.service.PostService;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Li
 * @Date 2022/7/14 0:06
 */
@Controller
@CrossOrigin
@Api(tags = "帖子评论")
public class PostCommentController {
    @Autowired
    private PostCommentService postCommentService;

    @ApiOperation(value = "评论帖子")
    @PostMapping(value = "/user/addPostComment")
    @ResponseBody
    public R addPostComment(Integer userId,Integer postId,String commentContent){
        return postCommentService.addComment(userId,postId,commentContent);
    }

    @ApiOperation(value = "回复评论")
    @PostMapping(value = "/user/responsePostComment")
    @ResponseBody
    public R responsePostComment(Integer userId,Integer commentId,String commentContent){
//        postCommentService.response(userId,commentFather,commentId,commentContent);
        return postCommentService.re(userId,commentId,commentContent);
    }

    @ApiOperation(value = "判断是否可以删除评论")
    @GetMapping(value = "/user/isDeleteComment")
    @ResponseBody
    public R isDeleteComment(Integer userId,Integer commentId,Integer postId){
        boolean isSelf = postCommentService.isDelete(userId, commentId,postId);
        if(isSelf)
            return new R(true,"可以删除");
        return new R(false,"不可以删除");
    }

    @ApiOperation(value = "删除帖子评论")
    @DeleteMapping(value = "/user/deletePostComment")
    @ResponseBody
    public R deletePostComment(Integer commentId){
        postCommentService.deleteComment(commentId);
        return new R(true,"删除成功");
    }


    @ApiOperation(value = "获取帖子全部评论")
    @GetMapping(value = "/getPostComment")
    @ResponseBody
    public R getPostComment(Integer postId){
        List<PostComment> postComments = postCommentService.viewComment(postId);
        return new R(true,postComments);
    }

    @ApiOperation(value = "点赞帖子评论")
    @GetMapping(value = "/user/likePostComment")
    @ResponseBody
    public R likePostComment(Integer userId,Integer commentId){
        return postCommentService.like(commentId,userId);
    }

    @ApiOperation(value = "判断是否已点赞")
    @GetMapping(value = "/user/isLikeComment")
    @ResponseBody
    public R isLikeComment(Integer userId,Integer commentId){
        if(postCommentService.isLike(commentId,userId))
            return new R(true,"已点赞");
        return new R(true,"未点赞");
    }

    /**
     *获取评论点赞数量
     * @date 2022/7/28 19:11
     * @param commentId 评论id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "获取评论点赞数量")
    @GetMapping(value = "/user/commentLikeNum")
    @ResponseBody
    public R commentLikeNum(Integer commentId){
        Integer like = postCommentService.commentLikeNum(commentId);
        return new R(true,like);
    }

}
