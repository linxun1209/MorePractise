package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xingchen.utils.RedisUtil;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Li
 * @Date 2022/7/6 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @TableId("post_id")
    private Integer postId;
    private Integer userId;
    @TableField(exist=false)
    private String userName;
    @TableField(exist=false)
    private String userHeadshotUrl;
    private String postTitle;
    private String postContent;
    private String postPicture;
    private String postType;
    private String postLabel;
    private Integer postVip;
    private String postTime;
    private Integer postState;
    @TableField(exist=false)
    private Long collection;
    @TableField(exist=false)
    private Long likes;
    private Integer postView;

    public Post(Integer userId, String postTitle, String postContent, String postTime, Integer postState) {
        this.userId = userId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTime = postTime;
        this.postState = postState;
    }

    public Post(Integer userId, String postTitle, String postContent, String postType, String postLabel, Integer postVip, String postTime, Integer postState) {
        this.userId = userId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postType = postType;
        this.postLabel = postLabel;
        this.postVip = postVip;
        this.postTime = postTime;
        this.postState = postState;
    }

    public Post( Integer userId, String postTitle, String postContent,String postPicture, String postType, String postLabel, Integer postVip, String postTime, Integer postState) {
        this.userId = userId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postPicture=postPicture;
        this.postType = postType;
        this.postLabel = postLabel;
        this.postVip = postVip;
        this.postTime = postTime;
        this.postState = postState;
    }

    public void nums(Post post,Long collection, Long likes,Integer view){
        post.setCollection(collection);
        post.setLikes(likes);
        if(view==null)
            view=0;
        Integer realView= post.getPostView() + view;
        post.setPostView(realView);
    }


}
