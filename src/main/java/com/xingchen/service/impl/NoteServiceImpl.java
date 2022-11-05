package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.NoteMapper;
import com.xingchen.pojo.Note;
import com.xingchen.pojo.User;
import com.xingchen.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {
    @Autowired
    private NoteMapper noteMapper;

    @Override
    @Transactional
    public boolean updateNote(Note note) {
        return noteMapper.updateById(note) > 0;
    }

    @Override
    @Transactional
    public IPage<Note> getUserNote(Integer userId, Integer currentPage, Integer pageSize) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        IPage<Note> page = new Page<>(currentPage, pageSize);
        IPage<Note> noteIPage = noteMapper.selectPage(page, queryWrapper);
        return noteIPage;
    }

    @Override
    public List<Note> viewNoteSelf(Integer userId) {
        return noteMapper.viewNoteSelf(userId);
    }

    @Override
    public Note getNote(Integer noteId) {
        Note note = noteMapper.getNote(noteId);
        return note;
    }
}




/**                 笔记不需要审核,只能用户自己看
    @Override
    public void noteAudit(Integer noteId, boolean audit) {
        Note note = noteMapper.selectById(noteId);
        if(!audit){
            note.setNoteStatue(-1);
        }
        else {
            note.setNoteStatue(1);
        }
        noteMapper.updateById(note);

    }


    @Override
    public IPage<Note> getAllNote(Integer noteStatue, Integer currentPage, Integer pageSize) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        if(noteStatue==0){
            queryWrapper.eq("note_statue",0);
        }else if(noteStatue==-1||noteStatue==1) {
            queryWrapper.eq("note_statue", 1).or().eq("note_statue", -1);
        }
        IPage<Note> page=new Page<>(currentPage,pageSize);
        IPage<Note> noteIPage = noteMapper.selectPage(page, queryWrapper);
        return noteIPage;
    }

 */

