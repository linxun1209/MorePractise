package com.xingchen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.pojo.Course;
import com.xingchen.pojo.Professional;
import com.xingchen.service.ICourseService;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 接口逻辑处理的基本没有问题
 */
@Controller
@CrossOrigin
@Api(tags = "课程")
public class CourseController {
    @Autowired
    private ICourseService iCourseService;
    boolean flag=true;

    /**
     * 管理员添加专业下的学科
     * @param professionalId
     * @param courseName
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "管理员添加专业下的学科")
    @PostMapping(value = "/administrator/saveProfessionalCourse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "professionalId",value = "专业id",required = true),
            @ApiImplicitParam(name = "courseName",value = "课程名"),
    })
    @ResponseBody
    public R saveProfessionalCourse(Integer professionalId,String courseName) throws IOException {
        Course course=new Course(professionalId,courseName);
        QueryWrapper<Course> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("professional_id",professionalId);
        courseQueryWrapper.eq("course_name", courseName);
        List<Course> courseList = iCourseService.list(courseQueryWrapper);
        if(courseList.isEmpty()){
            boolean flag = iCourseService.save(course);
            return new R(flag, flag ? "添加成功^_^" : "添加失败,该专业下学科已存在-_-!");
        }
        return new R(flag, flag ? "添加成功^_^" : "添加失败,该专业下学科已存在-_-!");
    }


    /**
     * 管理员删除某专业的学科
     * @param courseId
     * @return
     */
    @ApiOperation(value = "管理员删除某专业的学科")
    @DeleteMapping(value = "/administrator/deleteProfessionalCourseId")
    @ApiImplicitParam(name = "courseId",value = "课程id",required = true)
    @ResponseBody
    public R deleteProfessionalCourseId(Integer courseId){
        return new R(true,iCourseService.delete(courseId));
    }

    /**
     * 管理员对专业进行批量删除
     * @param CourseId
     * @return
     */
    @ApiOperation(value = "管理员对学科进行批量删除")
    @DeleteMapping(value = "/administrator/deleteMoreCourse")
    @ResponseBody
    public R deleteMoreCourse(Integer[] CourseId){
        return new R(iCourseService.deleteMore(CourseId));
    }



    /**
     * 管理员修改专业下的学科
     * @param course
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "管理员修改专业下的学科")
    @PutMapping(value = "/administrator/updateProfessionalCourse")
    @ResponseBody
    public R updateProfessionalCourse(Course course) {
        boolean flag = iCourseService.updateCourse(course);
        return new R(flag, flag ? "修改成功^_^" : "修改失败,该专业下学科已存在-_-!");
    }



    /**
     * 查询专业下的所有学科
     * @param professionalId
     * @return
     */
    @ApiOperation(value = "查询专业下的所有学科")
    @PostMapping(value = "/getProfessionalCourse")
    @ApiImplicitParam(name = "professionalId",value = "专业id",required = true)
    @ResponseBody
    public R getProfessionalCourse(Integer professionalId){
        List<Course> courseList = iCourseService.getAllProfessionalCourse(professionalId);
        if(courseList.isEmpty()){
            flag=false;
        }
        return new R(flag,courseList);
    }



    /**
     * 分页查询专业对应的学科
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询专业对应的学科")
    @GetMapping(value = "/getPageProfessionalCourse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "professionalId",value = "专业id",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R getPageProfessionalCourse(Integer professionalId,Integer currentPage,Integer pageSize) {
        IPage<Course> page = iCourseService.getPageCourse(professionalId,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iCourseService.getPageCourse(professionalId,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }

    /**
     * 分页查询所有的学科
     * @return
     */
    @ApiOperation(value = "分页查询所有的学科")
    @GetMapping(value = "/getPageAllCourse")
    @ResponseBody
    public R getPageAllCourse() {
        Set<String> pageAllCourse = iCourseService.getPageAllCourse();
        return new R(true, pageAllCourse);
    }



    @ApiOperation(value = "分页搜索专业对应的学科")
    @GetMapping(value = "/administrator/SearchPageProfessionalCourse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "professionalId",value = "专业id",required = true),
            @ApiImplicitParam(name = "search",value = "课程名",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R SearchPageProfessionalCourse(Integer professionalId,String search,Integer currentPage,Integer pageSize) {
        IPage<Course> page = iCourseService.searchPageCourse(professionalId,search,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iCourseService.searchPageCourse(professionalId,search,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }




}
