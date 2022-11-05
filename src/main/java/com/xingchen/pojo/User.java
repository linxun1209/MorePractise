package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId("user_id")
    private Integer userId;
    private String userAccount;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private Integer userExperience;
    private String userSex;
    private String userBirthday;
    private String userIntroduce;
    private String userAddress;
    private String userSchool;
    private Integer userVip;
    private Integer userAuthority;
    private String userHeadshotUrl;
    @TableField(exist=false)
    private Integer follow;
    @TableField(exist=false)
    private Integer fan;

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Integer userId, String userName, String userSex, String userBirthday, String userIntroduce, String userAddress, String userSchool) {
        this.userId=userId;
        this.userName = userName;
        this.userSex = userSex;
        this.userBirthday = userBirthday;
        this.userIntroduce = userIntroduce;
        this.userAddress = userAddress;
        this.userSchool = userSchool;
    }

    public User(String userAccount, String userName, String userPassword, String userEmail,String userIntroduce) {
        this.userAccount = userAccount;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userIntroduce=userIntroduce;
    }
}

