package com.xingchen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.dao.NoteMapper;
import com.xingchen.pojo.Note;
import com.xingchen.service.INoteService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * 接口逻辑处理的基本没有问题
 */
@Controller
@CrossOrigin
@Api(tags = "笔记")
public class NoteController {

    @Autowired
    private INoteService iNoteService;
    @Autowired
    private NoteMapper noteMapper;

    /**
     * 练习完成界面要有笔记
     *
     * @param topicId
     * @param noteContent
     * @param userId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "练习完成界面要有笔记")
    @PostMapping(value = "/user/saveTopicNote")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId", value = "题目id", required = true),
            @ApiImplicitParam(name = "noteContent", value = "笔记内容", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    })
    @ResponseBody
    public R saveTopicNote(Integer topicId, String noteContent, Integer userId) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Note note = new Note(topicId, noteContent, userId, format.format(date));
        boolean flag = iNoteService.save(note);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }


    /**
     * 用户删除做题笔记
     * @param noteId
     * @return
     */
    @ApiOperation(value = "用户删除做题笔记")
    @DeleteMapping(value = "/user/deleteNote")
    @ResponseBody
    public R deleteNote(Integer noteId) {
        return new R(iNoteService.removeById(noteId));
    }

    /**
     * 用户批量删除做题笔记
     *
     * @param noteId
     * @return
     */
    @ApiOperation(value = "用户批量删除做题笔记")
    @DeleteMapping(value = "/user/deleteMoreNote")
    @ResponseBody
    public R deleteMoreNote(Integer[] noteId) {
        return new R(iNoteService.removeBatchByIds(Arrays.asList(noteId)));
    }

    /**
     * 用户修改发布的笔记
     *
     * @param noteId
     * @param topicId
     * @param noteContent
     * @param userId
     * @return
     */
    @ApiOperation(value = "用户修改发布的笔记")
    @PutMapping(value = "/user/updateNote")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "NoteId", value = "笔记id", required = true),
            @ApiImplicitParam(name = "topicId", value = "题目id", required = true),
            @ApiImplicitParam(name = "noteContent", value = "笔记内容", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),

    })
    @ResponseBody
    public R updateNote(Integer noteId, Integer topicId, String noteContent, Integer userId) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Note note = new Note(noteId, topicId, noteContent, userId, format.format(date));
        boolean flag = iNoteService.updateNote(note);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }

    /**
     * 查看用户自己的笔记
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看用户自己的笔记(分页)(需要自己渲染用户图片和头像)")
    @GetMapping(value = "/user/getUserNote")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "currentPage", value = "当前页数", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页个数", required = true)
    })
    @ResponseBody
    public R getUserNote(Integer userId, Integer currentPage, Integer pageSize) {
        IPage<Note> page = iNoteService.getUserNote(userId, currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iNoteService.getUserNote(userId, (int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }


    /**
     * 查看用户自己的笔记
     * @param userId
     * @return
     */
    @ApiOperation(value = "查看用户自己的笔记(不分页但是带上用户图像和用户名)")
    @GetMapping(value = "/user/viewNoteSelf")
    @ResponseBody
    public R viewNoteSelf(Integer userId) {
        List<Note> noteList = iNoteService.viewNoteSelf(userId);
        return new R(true, noteList);
    }

    /**
     * 该笔记
     * @param noteId
     * @return
     */
    @ApiOperation(value = "该笔记")
    @GetMapping(value = "/getNote")
    @ResponseBody
    public R getNote(Integer noteId) {
        Note note = iNoteService.getNote(noteId);
        return new R(true,note);
    }
}

    /**      做题笔记只能用户自己看到
     * 管理员获取对应状态的笔记
     * @param noteStatue
     * @param currentPage
     * @param pageSize
     * @return

    @ApiOperation(value = "管理员获取对应状态的笔记")
    @GetMapping(value = "/administrator/getAllNote")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noteStatue",value = "笔记状态",required = true),
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R getAllNote(Integer noteStatue, Integer currentPage,Integer pageSize){
        IPage<Note> page = iNoteService.getAllNote(noteStatue,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iNoteService.getAllNote(noteStatue,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }


     */




    /**
     * 审核笔记
     * @param NoteId
     * @param audit
     * @return

    @ApiOperation(value = "审核笔记")
    @PostMapping(value = "/administrator/NoteAudit")
    @ApiImplicitParam(name = "audit",value = "审核结果，true为通过审核，false为未通过",required = true)
    @ResponseBody
    public R NoteAudit(Integer NoteId,boolean audit){
        iNoteService.noteAudit(NoteId,audit);
        return new R(true,"审核成功");
    }
     */

