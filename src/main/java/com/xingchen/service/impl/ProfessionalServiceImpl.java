package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.ProfessionalMapper;
import com.xingchen.pojo.Professional;

import com.xingchen.service.IProfessionalService;

import com.xingchen.utils.R;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfessionalServiceImpl extends ServiceImpl<ProfessionalMapper, Professional> implements IProfessionalService {
    @Autowired
    private ProfessionalMapper professionalMapper;
    @Override
    @Transactional
    public boolean modify(Professional professional) {
        QueryWrapper<Professional> courseQueryWrapper=new QueryWrapper<>();
        String professionalName = professional.getProfessionalName();
        courseQueryWrapper.eq("professional_name",professionalName);
        List<Professional> professionals = professionalMapper.selectList(courseQueryWrapper);
        if(professionals.isEmpty()){
            professionalMapper.updateById(professional);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(Integer professionalId) {
        return professionalMapper.deleteById(professionalId)>0;
    }

    @Override
    @Transactional
    public boolean deleteMore(Integer[] professionalId) {
        return professionalMapper.deleteBatchIds(Arrays.asList(professionalId))>0;
    }

    @Override
    @Transactional
    public IPage<Professional> getPageProfessional(Integer currentPage, Integer pageSize) {
        IPage page=new Page(currentPage,pageSize);
        professionalMapper.selectPage(page,null);
        return page;
    }

    @Override
    public IPage<Professional> searchProfession(Integer currentPage, Integer pageSize, String search) {
        QueryWrapper<Professional> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("professional_name",search);
        IPage<Professional> page=new Page<>(currentPage,pageSize);
        professionalMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public R saveProfessional(String professionalName) {
        Professional professional = new Professional(professionalName);
        QueryWrapper<Professional> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("professional_name", professionalName);
        List<Professional> professionals = professionalMapper.selectList(courseQueryWrapper);
        if (professionals.isEmpty()) {
            professionalMapper.insert(professional);
            return new R(true, "添加成功^_^");
        } else {
            return new R(false, "添加失败,该专业已存在-_-!");
        }
    }
}
