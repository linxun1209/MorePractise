package com.xingchen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.pojo.Answer;
import com.xingchen.pojo.Topic;
import com.xingchen.service.IAnswerService;
import com.xingchen.service.ITopicService;
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
import java.util.List;

@Controller
@CrossOrigin
@Api(tags = "刷题")
public class AnswerController {
    @Autowired
    private IAnswerService iAnswerService;
    @Autowired
    private ITopicService topicService;

    /**
     *
     * @param topicId
     * @param answerUid
     * @param answerContent
     * @return
     * @throws IOException
     */

    @ApiOperation(value = "答题")
    @PostMapping(value = "/user/saveTopicAnswer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId",value = "题目id",required = true),
            @ApiImplicitParam(name = "answerUid",value = "回答者id",required = true),
            @ApiImplicitParam(name = "answerContent",value = "回答内容",required = true)
    })
    @ResponseBody
    public R saveTopicAnswer(Integer topicId,Integer answerUid, String answerContent){
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Answer answer=new Answer(topicId,answerUid,answerContent,format.format(date));
        Topic byId = topicService.getById(topicId);
        String successResult = byId.getSuccessResult();
        if(successResult.equals(answerContent)){
            answer.setAnswerResult(1);
            iAnswerService.save(answer);
            return new R(true,  "回答正确^_^");
        }
        answer.setAnswerResult(0);
        iAnswerService.save(answer);
        return new R(true, "回答错误-_-!");
    }

    /**
     * 修改回答
     * @param answerId
     * @param topicId
     * @param topicUid
     * @param answerContent
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "修改回答(正常来说答题的结果是不能被修改的,所以该接口多余)")
    @PutMapping(value = "/user/updateTopicAnswer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "answerId",value = "回答id",required = true),
            @ApiImplicitParam(name = "topicId",value = "题目id",required = true),
            @ApiImplicitParam(name = "topicUid",value = "用户id",required = true),
            @ApiImplicitParam(name = "answerContent",value = "回答内容",required = true)
    })
    @ResponseBody
    public R updateTopicAnswer(Integer answerId,Integer topicId,Integer topicUid, String answerContent) throws IOException {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Answer answer=new Answer(answerId,topicId,topicUid,answerContent,format.format(date));
        boolean flag = iAnswerService.updateAnswer(answer);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }

    /**
     * 查看用户自己的解题
     * @param topicId
     * @return
     */
    @ApiOperation(value = "查看用户自己的解题")
    @GetMapping(value = "/user/viewAnswerSelf")
    @ResponseBody
    public R viewAnswerSelf(Integer topicId,Integer answerUid){
        List<Answer> answers = iAnswerService.viewAnswerSelf(topicId,answerUid);
        return new R(true,answers);
    }


    /**
     * 查看解题
     * @param answerId
     * @return
     */
    @ApiOperation(value = "查看解题")
    @GetMapping(value = "/viewAnswer")
    @ResponseBody
    public R viewAnswer(Integer answerId){
        Answer answers = iAnswerService.viewAnswer(answerId);
        return new R(true,answers);
    }


    /**
     * 获得用户刷过的所有题目id
     * @param answerUid
     * @return
     */

    @ApiOperation(value = "获得用户刷过的所有题目id")
    @GetMapping(value = "/getDoTopic")
    @ResponseBody
    public R getDoTopic(Integer answerUid){
        return new R(true,iAnswerService.getPassTopic(answerUid));

    }


    /**
     *  获得用户未刷过的所有题目id
     * @param answerUid
     * @return
     */

    @ApiOperation(value = "获得用户未刷过的所有题目id")
    @GetMapping(value = "/getNotTopic")
    @ResponseBody
    public R getNotTopic(Integer answerUid){
        return new R(true,iAnswerService.getNotTopic(answerUid));
    }




















    @ApiOperation(value = "查看解题")
    @GetMapping(value = "/viewTopicAnswer")
    @ResponseBody
    public R viewTopicAnswer(Integer topicId){
        List<Answer> answerList=iAnswerService.viewTopicAnswer(topicId);
        return new R(true,answerList);
    }



    /**
     * 获得用户自己的题目
     * @param answerUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获得用户自己的题目")
    @GetMapping(value = "/user/getUserAnswer")
    @ResponseBody
    public R getUserAnswer(Integer answerUid,Integer currentPage,Integer pageSize){
        IPage<Answer> page = iAnswerService.getUserAnswer(answerUid,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iAnswerService.getUserAnswer(answerUid,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }


    /**
     * 获得用户对该题成功的次数
     * @param topicId
     * @param answerUid
     * @return
     */
    @ApiOperation(value = "获得用户对该题成功的次数")
    @GetMapping(value = "/user/getSuccessCount")
    @ResponseBody
    public R getSuccessCount(Integer topicId,Integer answerUid){
        Long successCount = iAnswerService.getSuccessCount(topicId, answerUid);
        return new R(true,successCount);
    }


    /**
     * 获得用户对该题错误的次数
     * @param topicId
     * @param answerUid
     * @return
     */
    @ApiOperation(value = "获得用户对该题错误的次数")
    @GetMapping(value = "/user/getFailCount")
    @ResponseBody
    public R getFailCount(Integer topicId,Integer answerUid){
        Long successCount = iAnswerService.getFailCount(topicId, answerUid);
        return new R(true,successCount);
    }

    /**
     * 删除对应的解析
     * @param answerId
     * @return
     */
    @ApiOperation(value = "删除对应的答案")
    @DeleteMapping(value = "/user/deleteAnswer")
    @ApiImplicitParam(name = "commentId",value = "解析id",required = true)
    @ResponseBody
    public R deleteAnswer(Integer answerId){
        return new R(true,iAnswerService.deleteAnswer(answerId));
    }

    /**
     * 对解析进行批量删除
     * @param answerId
     * @return
     */
    @ApiOperation(value = "对答案进行批量删除")
    @DeleteMapping(value = "/user/deleteMoreAnswer")
    @ResponseBody
    public R deleteMoreAnswer(Integer[] answerId){
        return new R(iAnswerService.deleteMoreAnswer(answerId));
    }













}
