package com.xingchen.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchen.pojo.Conclusion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConclusionMapper extends BaseMapper<Conclusion> {


    List<Conclusion> viewConclusionSelf(Integer userId);

    List<Conclusion> getTopViews(QueryWrapper<Conclusion> courseQueryWrapper);
}
