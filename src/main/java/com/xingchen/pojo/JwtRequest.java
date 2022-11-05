package com.xingchen.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Li
 * @Date 2022/7/23 0:59
 */
@Data
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
}

