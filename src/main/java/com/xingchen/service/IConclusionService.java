package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Conclusion;
import com.xingchen.utils.R;


import java.util.List;

public interface IConclusionService extends IService<Conclusion> {
    List<Conclusion> viewConclusionSelf(Integer userId);

    IPage<Conclusion> getPageConclusion(Integer userId, Integer pages, Integer pageSize);


    void conclusionAudit(Integer conclusionId, boolean audit);
    IPage<Conclusion> getAllConclusion(Integer conclusionStatue, Integer currentPage, Integer pageSize);

    IPage<Conclusion> searchConclusionPageStatueOver(Integer currentPage, Integer pageSize, String search);

    IPage<Conclusion> searchConclusionPageStatue(Integer currentPage, Integer pageSize, String search);


    R ConclusionLike(Integer userId, Integer conclusionId);

    boolean ConclusionBooleanLike(Integer userId, Integer conclusionId);

    List<Conclusion> getConclusionLike(Integer userId);

    IPage<Conclusion> getPagePassConclusion(Integer userId, Integer currentPage, Integer pageSize,Integer conclusionStatue);

    Conclusion getConclusion(Integer conclusionId);

    boolean getViews(Integer conclusionId);

    List<Conclusion> getTopViews();

    List<Conclusion> getTopTime();

    List<Conclusion> getTopSelf();
}
