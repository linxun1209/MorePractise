package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Course;

import java.util.List;
import java.util.Set;

public interface ICourseService extends IService<Course> {
    //查
    List<Course> getAllProfessionalCourse(Integer professionalId);
    //改
    boolean updateCourse(Course course);
    //删
    boolean delete(Integer courseId);
    //批量删
    Boolean deleteMore(Integer[] courseId);
    IPage<Course> getPageCourse(Integer professionalId,Integer currentPage, Integer pageSize);

    Set<String> getPageAllCourse();

    IPage<Course> searchPageCourse(Integer professionalId, String search, Integer currentPage, Integer pageSize);
}
