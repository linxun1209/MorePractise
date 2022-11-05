package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Professional;
import com.xingchen.utils.R;


public interface IProfessionalService extends IService<Professional> {

    boolean modify(Professional professional);

    boolean delete(Integer professionalId);

    boolean deleteMore(Integer[] professionalId);

    IPage<Professional> getPageProfessional(Integer currentPage, Integer pageSize);

    IPage<Professional> searchProfession(Integer currentPage, Integer pageSize, String search);

    R saveProfessional(String  professionalName);
}
