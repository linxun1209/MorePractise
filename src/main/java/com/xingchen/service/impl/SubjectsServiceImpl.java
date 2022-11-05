package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.SubjectsMapper;
import com.xingchen.pojo.Subjects;
import com.xingchen.service.ISubjectsService;
import com.xingchen.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
public class SubjectsServiceImpl extends ServiceImpl<SubjectsMapper, Subjects> implements ISubjectsService {

    @Autowired
    private SubjectsMapper subjectsMapper;
    
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public boolean modifySubjects(Subjects subjects) {
        return subjectsMapper.updateById(subjects)>0;
    }

    @Override
    public boolean deleteSubjects(Integer subjectsId) {
        return subjectsMapper.deleteById(subjectsId)>0;
    }

    @Override
    public boolean deleteMoreSubjects(Integer[] subjectsId) {
        return subjectsMapper.deleteBatchIds(Arrays.asList(subjectsId))>0;
    }

    @Override
    public IPage<Subjects> getPageProfessional(Integer currentPage, Integer pageSize) {
        IPage page=new Page(currentPage,pageSize);
        subjectsMapper.selectPage(page,null);
        return page;
    }

    @Override
    public void addSubjects(String Subjects) {
        if(redisUtil.hasKey("Subjects")&&redisUtil.sHasKey("Subjects",Subjects))
            return;
        redisUtil.sSet("Subjects",Subjects);
        
    }

    @Override
    public void removeSubjects(String Subjects) {
        redisUtil.setRemove("Subjects",Subjects);

    }

    @Override
    public Set<Object> getSubjects() {
        if(!redisUtil.hasKey("Subjects"))
            return null;
        return redisUtil.sGet("Subjects");
    }
}