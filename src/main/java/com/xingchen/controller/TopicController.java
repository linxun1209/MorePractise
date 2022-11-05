package com.xingchen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.pojo.Topic;
import com.xingchen.service.ITopicService;
import com.xingchen.utils.R;
import com.xingchen.service.impl.WebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.util.List;

@Controller
@CrossOrigin
@Api(tags = "题库")
public class TopicController {
    @Autowired
    private ITopicService iTopicService;
    boolean flag=true;
    @Autowired
    private WebSocket webSocket;


    /**
     * 上传题目的相关操作
     */


    /**
     * 管理员发布题目
     * @param topicDifficulty
     * @param topicContent
     * @param topicCourse
     * @param topicProfessional
     * @param topicUid
     * @param topicName
     * @param topicType
     * @param successResult
     * @param failResult
     * @param topicVip
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "用户发布题目")
    @PostMapping(value = "/user/saveTopic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicDifficulty",value = "题目难易程度",required = true),
            @ApiImplicitParam(name = "topicContent",value = "题目内容",required = true),
            @ApiImplicitParam(name = "topicCourse",value = "题目学科类别",required = true),
            @ApiImplicitParam(name = "topicProfessional",value = "题目专业类别",required = true),
            @ApiImplicitParam(name = "topicUid",value = "题目作者id",required = true),
            @ApiImplicitParam(name = "topicName",value = "标题",required = true),
            @ApiImplicitParam(name = "topicType",value = "题目类型",required = true),
            @ApiImplicitParam(name = "successResult",value = "正确选秀",required = true),
            @ApiImplicitParam(name = "failResult",value = "题目选项",required = true),
            @ApiImplicitParam(name = "topicVip",value = "是否是vip",required = true)

    })
    @ResponseBody
    public R saveTopic(String topicDifficulty, String topicContent, String topicCourse, String topicProfessional, Integer topicUid, String topicName, String topicType,String successResult, String failResult,String topicVip) throws IOException {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Topic topic=new Topic(topicDifficulty,topicContent,topicCourse,topicProfessional,topicUid,format.format(date),topicName,topicType,successResult,failResult,topicVip);
        boolean flag = iTopicService.save(topic);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }

    /**
     * 管理员对发布的题进行修改
     * @param topicId
     * @param topicDifficulty
     * @param topicContent
     * @param topicCourse
     * @param topicProfessional
     * @param topicUid
     * @param topicName
     * @param topicType
     * @param successResult
     * @param failResult
     * @param topicVip
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "对发布的题进行修改")
    @PutMapping(value = "/all/updateTopic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId",value = "题目id",required = true),
            @ApiImplicitParam(name = "topicDifficulty",value = "题目难易程度",required = true),
            @ApiImplicitParam(name = "topicContent",value = "题目内容",required = true),
            @ApiImplicitParam(name = "topicCourse",value = "题目学科类别",required = true),
            @ApiImplicitParam(name = "topicProfessional",value = "题目专业类别",required = true),
            @ApiImplicitParam(name = "topicUid",value = "题目作者id",required = true),
            @ApiImplicitParam(name = "topicName",value = "标题",required = true),
            @ApiImplicitParam(name = "topicType",value = "题目类型",required = true),
            @ApiImplicitParam(name = "successResult",value = "正确选秀",required = true),
            @ApiImplicitParam(name = "failResult",value = "题目选项",required = true),
            @ApiImplicitParam(name = "topicVip",value = "粉丝用户id",required = true)

    })
    @ResponseBody
    public R updateTopic(Integer topicId,String topicDifficulty, String topicContent, String topicCourse, String topicProfessional, Integer topicUid , String topicName, String topicType,String successResult, String failResult,String topicVip) throws IOException {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Topic topic=new Topic(topicId,topicDifficulty,topicContent,topicCourse,topicProfessional,topicUid,format.format(date),topicName,topicType,successResult,failResult,topicVip);
        boolean flag = iTopicService.modify(topic);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }

    /**
     * 管理员对题目进行删除
     * @param topicId
     * @return
     */
    @ApiOperation(value = "对题目进行删除")
    @DeleteMapping( value = "/all/deleteTopic")
    @ApiImplicitParam(name = "topicId",value = "题目id",required = true)
    @ResponseBody
    public R deleteTopic(Integer topicId){
        return new R(iTopicService.delete(topicId));
    }

    /**
     * 管理员对题目进行批量删除
     * @param topicId
     * @return
     */
    @ApiOperation(value = "管理员和用户对题目进行批量删除")
    @DeleteMapping(value = "/all/deleteMoreTopic")
    @ResponseBody
    public R deleteMoreTopic(Integer[] topicId){
        return new R(iTopicService.deleteMore(topicId));
    }

    /**
     * 通过题目id来获取对应的题目
     * @param topicId
     * @return
     */
    @ApiOperation(value = "通过题目id来获取对应的题目")
    @GetMapping(value = "/getByTopicId")
    @ApiImplicitParam(name = "topicId",value = "题目id",required = true)
    @ResponseBody
    public R getByTopicId(Integer topicId){
        Topic byId = iTopicService.getById(topicId);
        boolean flag=true;
        if(byId==null){
            flag=false;
        }
        return new R(flag,byId);
    }

    /**
     * 用户来获取自己发布的题目
     * @param topicUid
     * @return
     */
    @ApiOperation(value = "用户来获取自己发布的题目")
    @GetMapping(value = "/user/getByUserId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicUid",value = "用户id",required = true),
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R getByUserId( Integer topicUid,Integer currentPage,  Integer pageSize){
        IPage<Topic> page = iTopicService.getListUserId(topicUid,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iTopicService.getListUserId(topicUid,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/getPageTopic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R getPageTopic( Integer currentPage,  Integer pageSize) {
        IPage<Topic> page = iTopicService.getPage1(currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iTopicService.getPage1((int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }

    /**
     * 管理员获得所有题目
     * @return
     */
    @ApiOperation(value = "管理员获得所有题目")
    @GetMapping(value = "/administrator/getAllTopic")
    @ResponseBody
    public R getAllTopic(){
        List<Topic> topicList = iTopicService.list();
        if(topicList.isEmpty()){
            flag=false;
        }
        return new R(flag,topicList);
    }

    /**
     * 用户获得所有审核通过的题目
     * @return
     */
    @ApiOperation(value = "用户获得所有审核通过的题目")
    @GetMapping(value = "/getAllPassTopic")
    @ResponseBody
    public R getAllPassTopic(){
        List<Topic> topicList = iTopicService.getAllPassTopic();
        if(topicList.isEmpty()){
            flag=false;
        }
        return new R(flag,topicList);
    }

    /**
     * 用户收藏题目
     * @param userId
     * @param topicId
     * @return
     */
    @ApiOperation(value = "用户收藏题目")
    @GetMapping(value = "/user/CollectionTopic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId",value = "题目id",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",required = true)
    })
    @ResponseBody
    public R CollectionTopic(Integer userId,Integer topicId){
        return iTopicService.CollectionTopic(topicId,userId);
    }




    @ApiOperation(value = "判断是否收藏改题目")
    @GetMapping(value = "/user/isCollectionTopic")
    @ResponseBody
    public R isCollectionTopic(Integer userId,Integer TopicId){
        boolean isCollectionTopic = iTopicService.isCollectionTopic(userId, TopicId);
        if(isCollectionTopic)
            return new R(true,"该题已被收藏");
        return new R(true,"该题未被收藏");
    }

    /**
     * 查看用户的收藏题目
     * @param topicUid
     * @return
     */
    @ApiOperation(value = "查看用户的收藏题目")
    @GetMapping(value = "/user/UserCollectionTopic")
    @ApiImplicitParam(name = "topicUid",value = "用户id",required = true)
    @ResponseBody
    public R UserCollectionTopic(Integer topicUid){
        List<Topic> topicList = iTopicService.UserCollectionTopic(topicUid);
        return new R(true,topicList);
    }


    /**
     * 查看用户的审核未审核的题目
     * @param topicUid
     * @param topicStatue
     * @return
     */
    @ApiOperation(value = "查看用户的审核未审核的题目")
    @GetMapping(value = "/user/getIsAudit")
    @ApiImplicitParam(name = "topicUid",value = "用户id",required = true)
    @ResponseBody
    public R getIsAudit(Integer topicUid,Integer topicStatue){
        List<Topic> topicList = iTopicService.getIsAudit(topicUid,topicStatue);
        return new R(true,topicList);
    }











    /**@ApiOperation(value = "查看用户的收藏题目")
    @GetMapping(value = "/user/getStatue")
    @ApiImplicitParam(name = "topicUid",value = "用户id",required = true)
    @ResponseBody
    public R getStatue(Integer topicStatue){
        List<Topic> topicList = iTopicService.getStatue(topicStatue);
        return new R(true,topicList);
    }
     */

    /**
     * 分类条件查询分页
     * @param topicType
     * @param topicProfessional
     * @param topicCourse
     * @param topicDifficulty
     * @param topicVip
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分类条件查询")
    @GetMapping(value = "/getByWays")
    @ResponseBody
    public R getByWays(String topicType,String topicProfessional,String topicCourse,String topicDifficulty,String topicVip ,Integer currentPage, Integer pageSize){
        Topic topic=new Topic(topicType,topicProfessional,topicCourse,topicDifficulty,topicVip);
        IPage<Topic> byWays = iTopicService.getByWays(topic, currentPage, pageSize);
        if (currentPage > byWays.getPages()) {
            byWays = iTopicService.getByWays(topic,(int) byWays.getPages(), pageSize);
        }
        return new R(flag,byWays);
    }



    /**
     *
     * @param topicId
     * @param audit   审核结果，true为通过审核，false为未通过
     * @return
     */
    @ApiOperation(value = "审核题目")
    @PostMapping(value = "/administrator/TopicAudit")
    @ApiImplicitParam(name = "audit",value = "审核结果，true为通过审核，false为未通过",required = true)
    @ResponseBody
    public R TopicAudit(Integer topicId,boolean audit){
        iTopicService.topicAudit(topicId,audit);
        return new R(true,"审核成功");
    }

    /**queryWrapper.like("topic_name",search).or().like("topic_content",search)
     * .or().like("topic_type",search).or().like("topic_difficulty",search)
     * .or().like("topic_course",search).or().like("topic_professional",search);
     * 分页模糊搜索题目
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    @ApiOperation(value = "分页模糊搜索题目")
    @GetMapping(value = "/searchTopicPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true),
            @ApiImplicitParam(name = "search",value = "搜索词",required = true)
    })
    @ResponseBody
    public R searchTopicPage(Integer currentPage,Integer pageSize,String search){
        IPage<Topic> page = iTopicService.searchByPage(currentPage, pageSize,search);
        if (currentPage > page.getPages()) {
            page = iTopicService.searchByPage((int) page.getPages(), pageSize,search);
        }
        return new R(true,page);
    }

    /**
     *管理员分页模糊搜索审核未通过状态下的题目
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    @ApiOperation(value = "管理员分页模糊搜索未审核状态下的题目")
    @GetMapping(value = "/administrator/searchTopicPageStatue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true),
            @ApiImplicitParam(name = "search",value = "搜索词",required = true)
    })
    @ResponseBody
    public R searchTopicPageStatue(Integer currentPage,Integer pageSize,String search){
        IPage<Topic> page = iTopicService.searchByPageStatue(currentPage, pageSize,search);
        if (currentPage > page.getPages()) {
            page = iTopicService.searchByPageStatue((int) page.getPages(), pageSize,search);
        }
        return new R(true,page);
    }

    /**
     * 管理员分页模糊搜索审核过的题目
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    @ApiOperation(value = "管理员分页模糊搜索审核过的题目")
    @GetMapping(value = "/administrator/searchTopicPageStatueOver")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true),
            @ApiImplicitParam(name = "search",value = "搜索词",required = true)
    })
    @ResponseBody
    public R searchTopicPageStatueOver(Integer currentPage,Integer pageSize,String search){
        IPage<Topic> page = iTopicService.searchByPageStatueOver(currentPage, pageSize,search);
        if (currentPage > page.getPages()) {
            page = iTopicService.searchByPageStatueOver((int) page.getPages(), pageSize,search);
        }
        return new R(true,page);
    }

    /**
     * 管理员获取题目
     * @param topicStatue
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "管理员获取题目")
    @GetMapping(value = "/administrator/getStatueTopic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicStatue",value = "题目状态",required = true),
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R getStatueTopic(Integer topicStatue, Integer currentPage,Integer pageSize){
        IPage<Topic> page = iTopicService. getAllTopic(topicStatue,currentPage, pageSize);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = iTopicService.getAllTopic(topicStatue,(int) page.getPages(), pageSize);
        }
        return new R(true, page);
    }


    /**
     * 刷题相关操作
     */

















}
