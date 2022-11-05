package com.xingchen.utils;

import lombok.Data;

import java.util.List;

/**
 * @author Li
 * @Date 2022/7/9 17:52
 */
@Data
public class Page<T> {
    private Integer size;
    private Integer total;
    private Integer current;
    private Integer page;
    private List<T> records;


    public boolean limit(){
        if(current<1||size<=0)
            return false;
        return true;
    }

    public boolean big(){
        if(page==0){
            current=1;
            return false;
        }
        else if(current>page){
            current=page;
            return true;
        }
        return false;
    }

    public Page(Integer size, Integer total, Integer current, Integer page, List<T> records) {
        this.size = size;
        this.total = total;
        this.current = current;
        this.page = page;
        this.records = records;
    }
}
