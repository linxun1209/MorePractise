package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Note;

import java.util.List;

public interface INoteService extends IService<Note> {


    boolean updateNote(Note note);

    IPage<Note> getUserNote(Integer userId, Integer currentPage, Integer pageSize);

    //用户查看自己的笔记
    List<Note> viewNoteSelf(Integer userId);

    Note getNote(Integer noteId);


//    void noteAudit(Integer noteId, boolean audit);

//    IPage<Note> getAllNote(Integer noteStatue, Integer currentPage, Integer pageSize);
}
