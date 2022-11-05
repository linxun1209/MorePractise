package com.xingchen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.pojo.Conclusion;
import com.xingchen.pojo.Note;
import com.xingchen.pojo.Post;
import com.xingchen.pojo.Topic;
import com.xingchen.service.IConclusionService;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * 接口逻辑处理的基本没有问题
 */
@Controller
@CrossOrigin
@Api(tags = "总结")
public class ConclusionController {

    @Autowired
    private IConclusionService iConclusionService;

    /**
     * 练习完成界面要有总结
     * @param conclusionContent
     * @param userId
     * @return
     */
    @ApiOperation(value = "练习完成界面要有总结")
    @PostMapping(value = "/user/saveTopicConclusion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "conclusionContent",value = "总结内容",required = true),
            @ApiImplicitParam(name = "conclusionVip",value = "vip",required = true),
            @ApiImplicitParam(name = "conclusionTitle",value = "标题",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",required = true)
    })
    @ResponseBody
    public R saveTopicConclusion(String conclusionContent, Integer userId,Integer conclusionVip,String conclusionTitle){
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Conclusion conclusion=new Conclusion(conclusionContent,userId,format.format(date),conclusionVip,conclusionTitle);
        boolean flag = iConclusionService.save(conclusion);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }

    /**
     * 删除对应的总结
     * @param ConclusionId
     * @return
     */
    @ApiOperation(value = "删除对应的总结")
    @DeleteMapping(value = "/all/deleteConclusion")
    @ResponseBody
    public R deleteConclusion(Integer ConclusionId){
        return new R(true,iConclusionService.removeById(ConclusionId));
    }


    /**
     * 用户批量删除做题总结
     * @param conclusionId
     * @return
     */
    @ApiOperation(value = "用户批量删除做题总结")
    @DeleteMapping(value = "/all/deleteMoreConclusion")
    @ResponseBody
    public R deleteMoreConclusion(Integer[] conclusionId){
        return new R(iConclusionService.removeBatchByIds(Arrays.asList(conclusionId)));
    }


    /**
     * 修改回答
     * @param ConclusionId
     * @param conclusionContent
     * @param userId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "修改回答")
    @PutMapping(value = "/user/updateConclusion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ConclusionId",value = "总结id",required = true),
            @ApiImplicitParam(name = "conclusionContent",value = "总结内容",required = true),
            @ApiImplicitParam(name = "conclusionTitle",value = "标题",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",required = true)
    })
    @ResponseBody
    public R updateConclusion(Integer ConclusionId,String conclusionContent, Integer userId,Integer conclusionVip,String conclusionTitle) throws IOException {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Conclusion conclusion=new Conclusion(ConclusionId,conclusionContent,userId,format.format(date),conclusionVip,conclusionTitle);
        boolean flag = iConclusionService.updateById(conclusion);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }


    /**
     * 查看用户自己的总结
     * @param userId
     * @return
     */
    @ApiOperation(value = "查看用户自己的总结")
    @PostMapping(value = "/user/viewConclusionSelf")
    @ResponseBody
    public R viewConclusionSelf(Integer userId){
        List<Conclusion> conclusions = iConclusionService.viewConclusionSelf(userId);
        return new R(true,conclusions);
    }


    /**
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询用户自己的总结")
    @GetMapping(value = "/user/getPageConclusion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R getPageConclusion(Integer userId,Integer currentPage,Integer pageSize) {
        IPage<Conclusion> page = iConclusionService.getPageConclusion(userId,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iConclusionService.getPageConclusion(userId,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }






    @ApiOperation(value = "分页查询用户自己审核通过未通过的总结")
    @GetMapping(value = "/user/getPagePassConclusion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",required = true)

    })
    @ResponseBody
    public R getPagePassConclusion(Integer userId,Integer currentPage,Integer pageSize,Integer conclusionStatue) {
        IPage<Conclusion> page = iConclusionService.getPagePassConclusion(userId,currentPage, pageSize,conclusionStatue);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iConclusionService.getPagePassConclusion(userId,(int) page.getPages(), pageSize,conclusionStatue);
        }
        return new R(true, page);
    }






    /**
     * 点赞总结（取消点赞）
     * @param userId
     * @param ConclusionId
     * @return
     */
    @ApiOperation(value = "点赞总结（取消点赞）")
    @GetMapping(value = "/user/ConclusionLike")
    @ResponseBody
    public R ConclusionLike(Integer userId,Integer ConclusionId){
        return iConclusionService.ConclusionLike(userId, ConclusionId);
    }



    @ApiOperation(value = "判断是否点赞该总结")
    @GetMapping(value = "/user/ConclusionBooleanLike")
    @ResponseBody
    public R ConclusionBooleanLike(Integer userId,Integer ConclusionId){
        boolean b = iConclusionService.ConclusionBooleanLike(userId, ConclusionId);
        if(b)
            return new R(true,"已点赞");
        return new R(true,"未点赞");
    }


    /**
     * 该总结
     * @param conclusionId
     * @return
     */
    @ApiOperation(value = "该总结")
    @PostMapping(value = "/getConclusion")
    @ResponseBody
    public R getConclusion(Integer conclusionId) {
        Conclusion conclusion=iConclusionService.getConclusion(conclusionId);
        return new R(true,conclusion);
    }


    /**
     * 总结的浏览量
     * @param conclusionId
     * @return
     */
    @ApiOperation(value = "总结的浏览量")
    @PutMapping(value = "/getViews")
    @ResponseBody
    public R getViews(Integer conclusionId) {
        iConclusionService.getViews(conclusionId);
        return new R(true);
    }


    /**
     * 浏览量排前9的总结
     * @return
     */
    @ApiOperation(value = "浏览量排前9的总结")
    @GetMapping(value = "/getTopViews")
    @ResponseBody
    public R getTopViews() {
        List<Conclusion> topViews = iConclusionService.getTopViews();
        return new R(true,topViews);
    }


    /**
     * 时间排前9的总结
     * @return
     */
    @ApiOperation(value = "时间排前9的总结")
    @GetMapping(value = "/getTopTime")
    @ResponseBody
    public R getTopTime() {
        List<Conclusion> topViews = iConclusionService.getTopTime();
        return new R(true,topViews);
    }


    /**
     * 推荐9个总结
     * @return
     */
    @ApiOperation(value = "推荐9个总结")
    @GetMapping(value = "/getTopSelf")
    @ResponseBody
    public R getTopSelf() {
        List<Conclusion> topViews = iConclusionService.getTopSelf();
        return new R(true,topViews);
    }






    /**
     * 管理员对总结的相关操作
     */


    /**
     * 管理员审核
     * @param conclusionId
     * @param audit
     * @return
     */
    @ApiOperation(value = "审核")
    @PostMapping(value = "/administrator/conclusionAudit")
    @ApiImplicitParam(name = "audit",value = "审核结果，true为通过审核，false为未通过",required = true)
    @ResponseBody
    public R conclusionAudit(Integer conclusionId,boolean audit){
        iConclusionService.conclusionAudit(conclusionId,audit);
        return new R(true,"审核成功");
    }


    /**
     * 管理员获取对应状态的总结
     * @param conclusionStatue
     * @param currentPage
     * @param pageSize
     * @return
     */

    @ApiOperation(value = "管理员获取对应状态的总结")
    @GetMapping(value = "/administrator/getAllConclusion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R getAllConclusion(Integer conclusionStatue, Integer currentPage,Integer pageSize){
        IPage<Conclusion> page = iConclusionService.getAllConclusion(conclusionStatue,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iConclusionService.getAllConclusion(conclusionStatue,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }


    /**
     * 管理员分页模糊搜索审核过的总结
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */

    @ApiOperation(value = "管理员分页模糊搜索审核过的总结")
    @GetMapping(value = "/administrator/searchConclusionPageStatueOver")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true),
            @ApiImplicitParam(name = "search",value = "搜索词",required = true)
    })
    @ResponseBody
    public R searchConclusionPageStatueOver(Integer currentPage,Integer pageSize,String search){
        IPage<Conclusion> page = iConclusionService.searchConclusionPageStatueOver(currentPage, pageSize,search);
        if (currentPage > page.getPages()) {
            page = iConclusionService.searchConclusionPageStatueOver((int) page.getPages(), pageSize,search);
        }
        return new R(true,page);
    }

    /**
     * 管理员分页模糊搜索未审核的总结
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */

    @ApiOperation(value = "管理员分页模糊搜索未审核的总结")
    @GetMapping(value = "/administrator/searchConclusionPageStatue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true),
            @ApiImplicitParam(name = "search",value = "搜索词",required = true)
    })
    @ResponseBody
    public R searchConclusionPageStatue(Integer currentPage,Integer pageSize,String search){
        IPage<Conclusion> page = iConclusionService.searchConclusionPageStatue(currentPage, pageSize,search);
        if (currentPage > page.getPages()) {
            page = iConclusionService.searchConclusionPageStatue((int) page.getPages(), pageSize,search);
        }
        return new R(true,page);
    }





}
