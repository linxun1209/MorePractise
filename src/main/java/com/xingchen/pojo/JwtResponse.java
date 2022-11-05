package com.xingchen.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Li
 * @Date 2022/7/23 0:59
 */
@Data
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String jwttoken;
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}

