package com.xingchen.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchen.pojo.Msg;
import com.xingchen.pojo.Topic;
import com.xingchen.service.MsgService;
import com.xingchen.utils.R;
import com.xingchen.service.impl.WebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 管理员发送消息发送控制器
 */
@Controller
@CrossOrigin
@Api(tags = "系统消息")
public class SendMessageController {

    @Autowired
    private WebSocket webSocket;
    @Autowired
    private MsgService msgService;

    /**
     * 管理员发动系统消息
     * @param msg
     * @return
     */
    @ApiOperation(value = "管理员发送系统消息")
    @PostMapping(value = "/administrator/sendInfo")
    @ResponseBody
    public String sendInfo(@RequestParam String msg) {
        try {
            webSocket.sendInfo(msg);
            Date date=new Date();
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Msg msg1=new Msg(msg,format.format(date));
            msgService.save(msg1);
        } catch (Exception e) {
            e.printStackTrace();
            return "信息发送异常!";
        }
        return "发送成功~";
    }

    /**
     * 分页获取历史系统消息
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页获取历史系统消息")
    @GetMapping(value = "/all/allPageMes")
    @ResponseBody
    public R allPageMes(Integer currentPage,  Integer pageSize ){
        IPage<Msg> page = msgService.allMes(currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = msgService.allMes((int) page.getPages(), pageSize);
        }
        return new R(true, page);

    }

    /**
     * 获取历史系统消息
     * @return
     */
    @ApiOperation(value = "获取历史系统消息")
    @GetMapping(value = "/all/allMes")
    @ResponseBody
    public R allMes( ){
        List<Msg> list = msgService.list();
        return new R(true,list);
    }



    /**
     * 删除对应的系统信息
     * @param messageId
     * @return
     */
    @ApiOperation(value = "删除对应的系统信息")
    @DeleteMapping(value = "/administrator/deleteMsg")
    @ApiImplicitParam(name = "messageId",value = "解析id",required = true)
    @ResponseBody
    public R deleteMsg(Integer messageId){
        return new R(true,msgService.removeById(messageId));
    }


    /**
     * 批量删除对应的系统信息
     * @param messageId
     * @return
     */
    @ApiOperation(value = "批量删除对应的系统信息")
    @DeleteMapping(value = "/administrator/deleteMoreMsg")
    @ResponseBody
    public R deleteMoreMsg(Integer[] messageId){
        return new R(msgService.removeBatchByIds(Arrays.asList(messageId)));
    }


    /**
     * 修改通知状态
     * @param messageId
     * @return
     */
    @ApiOperation(value = "查看通知")
    @PutMapping(value = "/all/isWatch")
    @ResponseBody
    public R isWatch(Integer messageId){
        msgService.isWatch(messageId);
        return new R(true,"已查看");
    }


































}