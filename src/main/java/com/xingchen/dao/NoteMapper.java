package com.xingchen.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchen.pojo.Note;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteMapper extends BaseMapper<Note> {
    List<Note> viewNoteSelf(Integer userId);

    Note getNote(Integer noteId);
}
