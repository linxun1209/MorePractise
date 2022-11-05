package com.xingchen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.dao.ProfessionalMapper;

import com.xingchen.pojo.Professional;

import com.xingchen.service.IProfessionalService;
import com.xingchen.utils.R;
import com.xingchen.service.impl.WebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 接口逻辑处理的基本没有问题
 */
@Controller
@CrossOrigin
@Api(tags = "专业")
public class ProfessionalController {
    @Autowired
    private IProfessionalService iProfessionalService;
    @Autowired
    private ProfessionalMapper professionalMapper;
    boolean flag = true;

    /**
     *
     * @return
     */
    @ApiOperation(value = "获取全部专业")
    @GetMapping(value = "/getAllProfessional")
    @ResponseBody
    public R getAllProfessional() {
        return new R(true,iProfessionalService.list());


    }





    /**
     * 管理员添加专业
     *
     * @param professionalName
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "管理员添加专业")
    @PostMapping(value = "/administrator/saveProfessional")
    @ApiImplicitParam(name = "professionalName", value = "专业名字")
    @ResponseBody
    public R saveProfessional(String professionalName) {
        return new R(true, iProfessionalService.saveProfessional(professionalName));

    }


    /**
     * 管理员修改专业
     *
     * @param professional
     * @return
     */
    @ApiOperation(value = "管理员修改专业")
    @PostMapping(value = "/administrator/updateProfessional")
    @ResponseBody
    public R updateProfessional(Professional professional) {
        boolean flag = iProfessionalService.modify(professional);
        return new R(flag, flag ? "修改成功^_^" : "修改失败,专业已存在-_-!");
    }

    /**
     * 管理员删除专业
     *
     * @param professionalId
     * @return
     */
    @ApiOperation(value = "管理员删除专业")
    @DeleteMapping(value = "/administrator/deleteProfessional")
    @ApiImplicitParam(name = "professionalId", value = "专业id", required = true)
    @ResponseBody
    public R deleteProfessional(Integer professionalId) {
        return new R(true, iProfessionalService.delete(professionalId));
    }

    /**
     * 管理员对专业进行批量删除
     *
     * @param professionalId
     * @return
     */
    @ApiOperation(value = "管理员对专业进行批量删除")
    @DeleteMapping(value = "/administrator/deleteMoreProfession")
    @ResponseBody
    public R deleteMoreProfession(Integer[] professionalId) {
        return new R(iProfessionalService.deleteMore(professionalId));
    }

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/administrator/getPageProfessional")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页数", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页个数", required = true)
    })
    public R getPageProfessional(Integer currentPage, Integer pageSize) {
        IPage<Professional> page = iProfessionalService.getPageProfessional(currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iProfessionalService.getPageProfessional((int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }


    /**
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    @ApiOperation(value = "管理员模糊搜索专业")
    @GetMapping(value = "/administrator/searchProfession")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页数", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页个数", required = true),
            @ApiImplicitParam(name = "search", value = "搜索词", required = true)
    })
    @ResponseBody
    public R searchProfession(Integer currentPage, Integer pageSize, String search) {
        IPage<Professional> page = iProfessionalService.searchProfession(currentPage, pageSize, search);
        if (currentPage > page.getPages()) {
            page = iProfessionalService.searchProfession((int) page.getPages(), pageSize, search);
        }
        return new R(true, page);
    }
}






    /**
     * 获取全部专业
     *
     * @return

    @ApiOperation(value = "获取全部专业")
    @GetMapping(value = "/all/getAllProfessional")
    @ResponseBody
    public R getAllProfessional() {
        QueryWrapper<Professional> courseQueryWrapper = new QueryWrapper<>();
        List<Professional> professionals = iProfessionalService.list(courseQueryWrapper);
        Set<String> professionalList = new HashSet<>();
        for (Professional professional : professionals) {
            String professionalName = professional.getProfessionalName();
            professionalList.add(professionalName);
        }
        if (professionalList.isEmpty())
            flag = false;
        return new R(flag, professionalList);
    }
    try{
        webSocket.sendInfo("新添加了的专业是:"+professionalName);
    }catch (Exception e){
        e.printStackTrace();
    }
     */



