package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.ConclusionMapper;
import com.xingchen.pojo.Conclusion;
import com.xingchen.pojo.Note;
import com.xingchen.pojo.Topic;
import com.xingchen.service.IConclusionService;
import com.xingchen.utils.R;
import com.xingchen.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ConclusionServiceImpl extends ServiceImpl<ConclusionMapper, Conclusion> implements IConclusionService {

    @Autowired
    private ConclusionMapper conclusionMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<Conclusion> viewConclusionSelf(Integer userId) {
        /**
        QueryWrapper<Conclusion> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Conclusion> conclusions = conclusionMapper.selectList(queryWrapper);
        return conclusions;
         **/

        return conclusionMapper.viewConclusionSelf(userId);


    }

    @Override
    public IPage<Conclusion> getPageConclusion(Integer userId, Integer currentPage, Integer pageSize) {
        QueryWrapper<Conclusion> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("user_id",userId);
        IPage page=new Page(currentPage,pageSize);
        conclusionMapper.selectPage(page,courseQueryWrapper);
        return page;
    }

    @Override
    public void conclusionAudit(Integer conclusionId, boolean audit) {
        Conclusion conclusion = conclusionMapper.selectById(conclusionId);
        if(!audit){
            conclusion.setConclusionStatue(-1);
        }else {
            conclusion.setConclusionStatue(1);
        }
        conclusionMapper.updateById(conclusion);
    }

    @Override
    public IPage<Conclusion> getAllConclusion(Integer conclusionStatue, Integer currentPage, Integer pageSize) {
        QueryWrapper<Conclusion> queryWrapper = new QueryWrapper<>();
        if(conclusionStatue==0){
            queryWrapper.eq("conclusion_statue",0);
        }else if(conclusionStatue==-1||conclusionStatue==1) {
            queryWrapper.eq("conclusion_statue", 1).or().eq("conclusion_statue", -1);
        }
        IPage<Conclusion> page=new Page<>(currentPage,pageSize);
        IPage<Conclusion> conclusionIPage = conclusionMapper.selectPage(page, queryWrapper);
        return conclusionIPage;
    }

    @Override
    public IPage<Conclusion> searchConclusionPageStatueOver(Integer currentPage, Integer pageSize, String search) {
        QueryWrapper<Conclusion> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("conclusion_statue",1);
        queryWrapper.like("conclusion_title",search);
        IPage<Conclusion> page=new Page<>(currentPage,pageSize);
        conclusionMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public IPage<Conclusion> searchConclusionPageStatue(Integer currentPage, Integer pageSize, String search) {
        QueryWrapper<Conclusion> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("conclusion_statue",0);
        queryWrapper.like("conclusion_title",search);
        IPage<Conclusion> page=new Page<>(currentPage,pageSize);
        conclusionMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public R ConclusionLike(Integer userId, Integer conclusionId) {
        String conclusionLikeKey="conclusion@Like:"+conclusionId;
        if(redisUtil.hasKey(conclusionLikeKey)&&redisUtil.sHasKey(conclusionLikeKey,userId)){
            redisUtil.setRemove(conclusionLikeKey,userId);
            return new R(true,"取消点赞");
        }
        redisUtil.sSet(conclusionLikeKey,userId);
        return new R(true,"点赞成功");
    }

    @Override
    public boolean ConclusionBooleanLike(Integer userId, Integer conclusionId) {
        String conclusionLikeKey="conclusion@Like:"+conclusionId;
        if(redisUtil.hasKey(conclusionLikeKey)&&redisUtil.sHasKey(conclusionLikeKey,userId))
            return true;
        return false;
    }

    @Override
    public List<Conclusion> getConclusionLike(Integer conclusionId) {
        String conclusionLikeKey="conclusion@Like:"+conclusionId;
        if(!redisUtil.hasKey(conclusionLikeKey))
            return null;
        Set<Object> objects = redisUtil.sGet(conclusionLikeKey);
        List<Integer> conclusionId1=new ArrayList(objects);
        System.out.println(conclusionId);
        for(int i:conclusionId1){
            QueryWrapper<Conclusion> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("conclusion_id",i);
            List<Conclusion> conclusions = conclusionMapper.selectList(queryWrapper);
            System.out.println(conclusions);
            return conclusions;
        }
        return null;
    }

    @Override
    public IPage<Conclusion> getPagePassConclusion(Integer userId, Integer currentPage, Integer pageSize,Integer conclusionStatue) {
        QueryWrapper<Conclusion> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("user_id",userId);
        courseQueryWrapper.eq("conclusion_statue",conclusionStatue);
        IPage page=new Page(currentPage,pageSize);
        conclusionMapper.selectPage(page,courseQueryWrapper);
        return page;


    }

    @Override
    public Conclusion getConclusion(Integer conclusionId) {
        Conclusion conclusion = conclusionMapper.selectById(conclusionId);
        return conclusion;
    }

    @Override
    public boolean getViews(Integer conclusionId) {
        Conclusion conclusion = conclusionMapper.selectById(conclusionId);
        Integer viewsCount = conclusion.getViewsCount();
        viewsCount=viewsCount+1;
        conclusion.setViewsCount(viewsCount);
        return conclusionMapper.updateById(conclusion)>0;
    }

    @Override
    public List<Conclusion> getTopViews() {
        QueryWrapper<Conclusion> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("views_count");
        courseQueryWrapper.eq("conclusion_statue",1);
        courseQueryWrapper.last("limit 12");
        List<Conclusion> conclusions = conclusionMapper.selectList(courseQueryWrapper);
        return conclusions;
    }

    @Override
    public List<Conclusion> getTopTime() {
        QueryWrapper<Conclusion> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("conclusion_time");
        courseQueryWrapper.eq("conclusion_statue",1);
        courseQueryWrapper.last("limit 12");
        List<Conclusion> conclusions = conclusionMapper.selectList(courseQueryWrapper);
        return conclusions;
    }

    @Override
    public List<Conclusion> getTopSelf() {
        QueryWrapper<Conclusion> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.gt("views_count",0);
        courseQueryWrapper.eq("conclusion_statue",1);
        courseQueryWrapper.last("limit 12");
        List<Conclusion> conclusions = conclusionMapper.selectList(courseQueryWrapper);
        return conclusions;
    }


}
