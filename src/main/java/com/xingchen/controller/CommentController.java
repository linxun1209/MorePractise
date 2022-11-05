package com.xingchen.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.pojo.Comment;

import com.xingchen.pojo.Notice;
import com.xingchen.service.ICommentService;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 接口逻辑处理的基本没有问题
 */
@Controller
@CrossOrigin
@Api(tags = "解析")
public class CommentController {
    @Autowired
    private ICommentService iCommentService;

    /**
     * 一级解析
     * @param topicId
     * @param commentContent
     * @param commenterId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "发布父解析")
    @PostMapping(value = "/user/saveComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId",value = "题目id",required = true),
            @ApiImplicitParam(name = "commentContent",value = "解析内容",required = true),
            @ApiImplicitParam(name = "commenterId",value = "解析者id",required = true)
    })
    @ResponseBody
    public R saveComment(Integer topicId, String commentContent, Integer commenterId) throws IOException {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Comment comment=new Comment(topicId,commentContent,commenterId,format.format(date),0);
        boolean flag = iCommentService.save(comment);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }











    /**
     * 对父解析进行删除
     * ***************(删除父评论的时候对应的子评论也会对应删除)*****************
     * @param commenterId
     * @param topicId
     * @return
     */
    @ApiOperation(value = "对父解析进行删除")
    @DeleteMapping(value = "/user/deleteFatherComment")
    @ResponseBody
    public R deleteFatherComment(Integer commenterId,Integer topicId,Integer commentId){
        return new R(iCommentService.deleteFatherComment(commenterId,topicId,commentId));
    }


    /**
     * 查看题目下面的父解析
     * @param topicId
     * @return
     */
    @ApiOperation(value = "查看题目下面的父解析")
    @GetMapping(value = "/user/getCommentFather")
    @ResponseBody
    public R getCommentFather(Integer topicId,Integer currentPage,Integer pageSize){
        IPage<Comment> page = iCommentService.getCommentFather(topicId,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iCommentService.getCommentFather(topicId,(int) page.getPages(), pageSize);
        }
        return new R(true, page);

    }


    /**
     * 回复解析
     * @param topicId
     * @param commentContent
     * @param commenterId
     * @param commenterSonId
     * @param commentStatue
     * @return
     */
    @ApiOperation(value = "回复解析")
    @PostMapping(value = "/user/saveCommentSon")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId",value = "题目id",required = true),
            @ApiImplicitParam(name = "commentContent",value = "解析内容",required = true),
            @ApiImplicitParam(name = "commenterId",value = "父用户id",required = true),
            @ApiImplicitParam(name = "commentStatue",value = "父解析id",required = true),
            @ApiImplicitParam(name = "commenterSonId",value = "子用户id",required = true)
    })
    @ResponseBody
    public R saveCommentSon(Integer topicId, String commentContent,Integer commenterId,Integer commenterSonId,Integer commentStatue) {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Comment comment=new Comment(topicId,commentContent,commenterId,format.format(date),commenterSonId,commentStatue);
        boolean flag = iCommentService.save(comment);
        return new R(flag, flag ? "回复成功^_^" : "回复失败-_-!");
    }




    /**
     * 删除对应的解析下解析(子评论)
     * @param commenterId
     * @param topicId
     * @param commenterSonId
     * @return
     */
    @ApiOperation(value = "删除对应的解析下解析(子评论)")
    @DeleteMapping(value = "/user/deleteCommentSon")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId",value = "题目id",required = true),
            @ApiImplicitParam(name = "commenterId",value = "父用户id",required = true),
            @ApiImplicitParam(name = "commentIdFather",value = "父解析id",required = true),
            @ApiImplicitParam(name = "commentId",value = "解析id",required = true),
            @ApiImplicitParam(name = "commenterSonId",value = "子用户id",required = true)
    })
    @ApiImplicitParam(name = "commentId",value = "解析id",required = true)
    @ResponseBody
    public R deleteCommentSon(Integer commenterId, Integer topicId,Integer commentIdFather,Integer commentId, Integer commenterSonId){
        return new R(true,iCommentService.deleteSonComment(commenterId,topicId,commentIdFather,commentId,commenterSonId));
    }





    @ApiOperation(value = "查看评论者下面的子解析")
    @GetMapping(value = "/user/getUserComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commenterId",value = "父评论用户id",required = true),
            @ApiImplicitParam(name = "commentId",value = "父评论的解析id",required = true)
    })
    @ResponseBody
    public R getUserComment(Integer commenterId,Integer commentId){
//        IPage<Comment> page = iCommentService.getUserComment(commenterId,commentId,currentPage, pageSize);
//        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
//        if (currentPage > page.getPages()) {
//            page = iCommentService.getUserComment(commenterId,commentId(int) page.getPages(), pageSize);
//        }
//        return new R(true, page);
        return new R(true, iCommentService.getUserComment(commenterId,commentId));
    }

    /**
     * 对解析进行删除
     * @param commentId
     * @return
     */
    @ApiOperation(value = "对解析进行删除(只输入题解评论id就可以删除,原则上刷题者(answerId)可以删除改回答下面的所有的评论")
    @DeleteMapping(value = "/user/deleteComment")
    @ResponseBody
    public R deleteComment(Integer commentId){
        return new R(iCommentService.deleteComment(commentId));
    }

    /**
     * 对解析进行批量删除
     * @param commentId
     * @return
     */
    @ApiOperation(value = "对解析进行批量删除")
    @DeleteMapping(value = "/user/deleteMoreComment")
    @ResponseBody
    public R deleteMoreComment(Integer[] commentId){
        return new R(iCommentService.deleteMoreComment(commentId));
    }



















//
//    @ApiOperation(value = "判断是否是自己的解析")
//    @GetMapping(value = "/user/isUserSelfComment")
//    @ResponseBody
//    public R isUserSelfComment(Integer id,Integer commentId){
//        iCommentService.isCommentSelf(id, commentId);
//        return new R(true,"不是该用户的评论");
//    }





    /**
     * 查看答案下面的解析
     * @param answerId
     * @return
     */
    /**@ApiOperation(value = "查看答案下面的解析")
     @PostMapping(value = "/user/getComment")
     @ResponseBody
     public R getComment(Integer answerId,Integer currentPage,Integer pageSize){
     IPage<Comment> page = iCommentService.getComment(answerId,currentPage, pageSize);
     //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
     if (currentPage > page.getPages()) {
     page = iCommentService.getComment(answerId,(int) page.getPages(), pageSize);
     }
     return new R(true, page);
     }*/
}
