package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.CourseMapper;
import com.xingchen.pojo.Course;
import com.xingchen.pojo.Topic;
import com.xingchen.service.ICourseService;
import com.xingchen.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;


    @Override
    @Transactional
    public List<Course> getAllProfessionalCourse(Integer professionalId) {
        QueryWrapper<Course> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("professional_id",professionalId);
        List<Course> courses = courseMapper.selectList(courseQueryWrapper);
        return courses;
    }

    @Override
    @Transactional
    public boolean updateCourse(Course course) {
        QueryWrapper<Course> courseQueryWrapper=new QueryWrapper<>();
        String courseName = course.getCourseName();
        Integer professionalId = course.getProfessionalId();
        courseQueryWrapper.eq("professional_id",professionalId);
        List<Course> courses2 = courseMapper.selectList(courseQueryWrapper);
        for(Course course1:courses2){
            String courseName1 = course1.getCourseName();
            if(!courseName1.equals(courseName)){
                courseMapper.updateById(course);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(Integer courseId) {
        return courseMapper.deleteById(courseId)>0;
    }

    @Override
    @Transactional
    public Boolean deleteMore(Integer[] courseId) {
        return courseMapper.deleteBatchIds(Arrays.asList(courseId))>0;

    }

    @Override
    public IPage<Course> getPageCourse(Integer professionalId,Integer currentPage, Integer pageSize) {
        QueryWrapper<Course> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("professional_id",professionalId);
        IPage page=new Page(currentPage,pageSize);
        courseMapper.selectPage(page,courseQueryWrapper);
        return page;
    }

    @Override
    public Set<String> getPageAllCourse() {
        QueryWrapper<Course> courseQueryWrapper=new QueryWrapper<>();
        List<Course> courses = courseMapper.selectList(courseQueryWrapper);
        Set<String> courses1=new HashSet<>();
        for(Course course:courses){
            String courseName = course.getCourseName();
            courses1.add(courseName);
        }
        return courses1;
    }

    @Override
    public IPage<Course> searchPageCourse(Integer professionalId, String search, Integer currentPage, Integer pageSize) {
        QueryWrapper<Course> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("professional_id",professionalId);
        courseQueryWrapper.like("course_name",search);
        IPage page=new Page(currentPage,pageSize);
        courseMapper.selectPage(page,courseQueryWrapper);
        return page;
    }
}
