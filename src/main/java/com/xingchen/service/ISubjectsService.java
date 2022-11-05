package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Subjects;

import java.util.Set;

public interface ISubjectsService extends IService<Subjects> {


    boolean modifySubjects(Subjects subjects);

    boolean deleteSubjects(Integer subjectsId);

    boolean deleteMoreSubjects(Integer[] subjectsId);

    IPage<Subjects> getPageProfessional(Integer currentPage, Integer pageSize);





    void addSubjects(String Subjects);

    void removeSubjects(String Subjects);


    Set<Object> getSubjects();
}