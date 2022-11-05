package com.xingchen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.pojo.Answer;
import com.xingchen.pojo.Note;
import com.xingchen.pojo.Notice;
import com.xingchen.pojo.User;
import com.xingchen.service.INoticeService;
import com.xingchen.service.UserService;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.controller
 * @date 2022/7/22 10:55
 */

@Controller
@CrossOrigin
@Api(tags = "消息通知")
public class NoticeController {

    @Autowired
    private INoticeService iNoticeService;


    /**
     *
     * @param toId
     * @param noticeContent
     * @param fromId
     * @param noticeStatue   /**当statue为 1是收藏    2是点赞    3是关注
     *
     * @return
     */

    @ApiOperation(value = "添加消息")
    @PostMapping(value = "/all/saveNotice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toId", value = "发送者id", required = true),
            @ApiImplicitParam(name = "noticeContent", value = "发送内容"),
            @ApiImplicitParam(name = "fromId", value = "接收者id", required = true)
    })
    @ResponseBody
    public R saveNotice(Integer toId, String noticeContent, Integer fromId,Integer noticeStatue) {
        iNoticeService.saveNotice(toId,noticeContent,fromId,noticeStatue);
        return new R(true,"添加成功");
    }


    /**
     *用户收到的消息
     * @param fromId
     * @param currentPage
     * @param pageSize
     * @return
     */

    @ApiOperation(value = "用户收到的消息")
    @GetMapping(value = "/all/getNoticeFrom")
    @ApiImplicitParam(name = "fromId", value = "接收者id", required = true)
    @ResponseBody
    public R getNoticeFrom(Integer fromId,Integer currentPage,Integer pageSize){
        IPage<Notice> page = iNoticeService.getNoticeFrom(fromId,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iNoticeService.getNoticeFrom(fromId,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }




    /**
     * 删除对应的通知
     * @param noticeId
     * @return
     */
    @ApiOperation(value = "删除对应的通知")
    @DeleteMapping(value = "/all/deleteNotice")
    @ResponseBody
    public R deleteNotice(Integer noticeId){
        return new R(true,iNoticeService.deleteNotice(noticeId));
    }

    /**
     * 对通知进行批量删除
     * @param noticeId
     * @return
     */
    @ApiOperation(value = "对通知进行批量删除")
    @DeleteMapping(value = "/all/deleteMoreNotice")
    @ResponseBody
    public R deleteMoreNotice(Integer[] noticeId) {
        return new R(iNoticeService.deleteMoreNotice(noticeId));
    }




    /**
     * 修改通知状态
     * @param noticeId
     * @return
     */
    @ApiOperation(value = "查看通知")
    @PutMapping(value = "/all/changeReadStatue")
    @ResponseBody
    public R changeReadStatue(Integer noticeId){

        return new R(true,iNoticeService.changeReadStatue(noticeId));
    }

    /**
     * 用户收到已读未读的消息
     * @param noticeReadStatue
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户收到已读未读的消息")
    @GetMapping(value = "/all/getNoticeRead")
    @ApiImplicitParam(name = "noticeReadStatue", value = "消息已读状态", required = true)
    @ResponseBody
    public R getNoticeRead(Integer noticeReadStatue,Integer currentPage,Integer pageSize,Integer fromId){
        IPage<Notice> page = iNoticeService.getNoticeRead(noticeReadStatue,currentPage, pageSize,fromId);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iNoticeService.getNoticeRead(noticeReadStatue,(int) page.getPages(), pageSize,fromId);
        }
        return new R(true, page);
    }


    /**
     * 用户该消息栏是否存在未读
     * @param fromId
     * @return
     */
    @ApiOperation(value = "用户该消息栏是否存在未读")
    @GetMapping(value = "/user/BooleanRead")
    @ApiImplicitParam(name = "fromId", value = "接收者id", required = true)
    @ResponseBody
    public R BooleanRead(Integer fromId){
        boolean flag = iNoticeService.booleanRead(fromId);
        return new R(flag, flag ? "存在未读^_^" : "全部已读-_-!");



    }
















}
