package com.xingchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.PostMapper;
import com.xingchen.dao.UserMapper;
import com.xingchen.pojo.Post;
import com.xingchen.pojo.User;
import com.xingchen.service.PostService;
import com.xingchen.utils.CosUtils;
import com.xingchen.utils.Page;
import com.xingchen.utils.R;
import com.xingchen.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Li
 * @date 2022/7/6 18:40
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;

    //存草稿
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDraft(Integer userId,String postTitle,String postContent,String postPicture) {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String postTime = simpleDateFormat.format(date);
        Post post=new Post(userId,postTitle,postContent,postTime,0);
        if(postPicture==null){
            postPicture="";
        }
        post.setPostPicture(postPicture);
        postMapper.insert(post);
        return true;
    }

    //上传图片
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String picture(MultipartFile file) {
        if(file==null)
            return null;
        return CosUtils.uploadCos("jpg,png,gif",file);
    }

    //发布帖子
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R postPublish(Integer userId, String postTitle, String postContent, String postPicture,String postType, String postLabel, Integer postVip) {
        if(postPicture==null){
            postPicture="";
        }
        if(postTitle==null||postTitle.equals("")){
            return new R(false,"帖子标题不能为空");
        }
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String postTime = simpleDateFormat.format(date);
        Post post=new Post(userId,postTitle,postContent,postPicture,postType,postLabel,postVip,postTime,1);
        postMapper.insert(post);
        return new R(true,"发布成功");
    }

    //审核帖子
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postAudit(Integer postId, boolean audit) {
        Post post = postMapper.selectById(postId);
        if(!audit){
            post.setPostState(2);
        }
        else {
            userMapper.addNum(post.getUserId(),50);
            post.setPostState(3);
        }
        postMapper.updateById(post);
    }

    //设置仅自己可见
    @Override
    public R onlySelf(Integer postId) {
        Post post = postMapper.selectById(postId);
        if(post.getPostState()==3){
            post.setPostState(4);
            postMapper.updateById(post);
            return new R(true,"设置成仅自己可见");
        }
        else if(post.getPostState()==4){
            post.setPostState(3);
            postMapper.updateById(post);
            return new R(true,"设置成全部可见");
        }
        else
            return new R(false,"帖子未审核");
    }

    //删除帖子
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postDelete(Integer postId) {
        postMapper.deleteById(postId);
    }

    //批量删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postDeleteMore(Integer[] postId) {
        List<Integer> list=Arrays.asList(postId);
        postMapper.deleteBatchIds(list);
    }

    //修改帖子内容
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Integer postId,String postTitle,String postContent,String postPicture,String postType,String postLabel,Integer postVip) {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String postTime = simpleDateFormat.format(date);
        Post post=new Post(postId,null,null,null,postTitle,postContent,postPicture,postType,postLabel,postVip,postTime,1,null,null,null);
        postMapper.updateById(post);
    }

    //查看单个帖子信息
    @Override
    public Post viewOne(Integer postId) {
        Post post = postMapper.viewOne(postId);
//        if(post.getPostVip()==1){
//            User user = userMapper.selectById(userId);
//            if(user.getUserVip()!=1&&user.getUserAuthority()!=1)
//                return null;
//        }
        if(redisUtil.hasKey("post@View:"+ postId)){
            redisUtil.incr("post@View:"+ postId,1);
        }
        else
            redisUtil.set("post@View:"+ postId,0);
        long collection = redisUtil.sGetSetSize("post@Collection:" + postId);
        long like = redisUtil.sGetSetSize("post@Like:" + postId);
        Integer view = (Integer) redisUtil.get("post@View:" + postId);
        post.nums(post,collection,like,view);
        return post;
    }

    //查看用户自己的帖子
    @Override
    public List<Post> viewSelf(Integer userId,Integer begin,String start,String end,String search) {
        List<Post> postList = postMapper.viewSelf(userId, begin, start, end, search);
        for (Post post:postList) {
            long collection = redisUtil.sGetSetSize("post@Collection:" + post.getPostId());
            long like = redisUtil.sGetSetSize("post@Like:" + post.getPostId());
            Integer view = (Integer) redisUtil.get("post@View:" + post.getPostId());
            post.nums(post,collection,like,view);
        }
        return postList;
    }

    //查看所有发布的帖子
    @Override
    public Page<Post> postPublish(Integer currentPage, Integer pageSize) {
        Integer begin=(currentPage-1)*pageSize;
        Integer total=postMapper.viewPublish(null,null).size();
        Integer page=total/pageSize;
        if(total%pageSize!=0||total==0)
            page++;
        Page<Post> post=new Page<>(pageSize,total,currentPage,page,null);
        if(!post.limit()){
            return null;
        }
        if(post.big()){
            begin=(post.getCurrent()-1)*pageSize;
        }
        List<Post> postList=postMapper.viewPublish(begin,pageSize);
        for (Post p:postList) {
            long collection = redisUtil.sGetSetSize("post@Collection:" + p.getPostId());
            long like = redisUtil.sGetSetSize("post@Like:" + p.getPostId());
            Integer view = (Integer) redisUtil.get("post@View:" + p.getPostId());
            p.nums(p,collection,like,view);
        }
        post.setRecords(postList);
        return post;
    }

    //分页查看帖子
    @Override
    public Page<Post> viewByPage(Integer currentPage, Integer pageSize,Integer state) {
        Integer begin=(currentPage-1)*pageSize;
        Integer total=postMapper.page(null,null,null,state).size();
        Integer page=total/pageSize;
        if(total%pageSize!=0||total==0)
            page++;
        Page<Post> post=new Page<>(pageSize,total,currentPage,page,null);
        if(!post.limit()){
            return null;
        }
        if(post.big()){
            begin=(post.getCurrent()-1)*pageSize;
        }
        List<Post> postList=postMapper.page(null,begin,pageSize,state);
        post.setRecords(postList);
        return post;
    }

    //分页搜索帖子
    @Override
    public Page<Post> searchByPage(Integer currentPage, Integer pageSize, String search,Integer state) {
        Integer begin=(currentPage-1)*pageSize;
        Integer total=postMapper.page(search,null,null,state).size();
        Integer page=total/pageSize;
        if(total%pageSize!=0||total==0)
            page++;
        Page<Post> post=new Page<>(pageSize,total,currentPage,page,null);
        if(!post.limit()){
            return null;
        }
        if(post.big()){
            begin=(post.getCurrent()-1)*pageSize;
        }
        List<Post> postList=postMapper.page(search,begin,pageSize,state);
        for (Post p:postList) {
            long collection = redisUtil.sGetSetSize("post@Collection:" + p.getPostId());
            long like = redisUtil.sGetSetSize("post@Like:" + p.getPostId());
            Integer view = (Integer) redisUtil.get("post@View:" + p.getPostId());
            p.nums(p,collection,like,view);
        }
        post.setRecords(postList);
        return post;
    }

    //收藏（取消收藏）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R collection(Integer userId, Integer postId) {
        String collectionKey="collection@Post:"+userId;
        String postKey="post@Collection:"+postId;
        if(redisUtil.hasKey(collectionKey)&&redisUtil.sHasKey(collectionKey,postId)){
            redisUtil.setRemove(collectionKey,postId);
            redisUtil.setRemove(postKey,userId);
            return new R(true,"取消收藏");
        }
        redisUtil.sSet(collectionKey,postId);
        redisUtil.sSet(postKey,userId);
        return new R(true,"收藏成功");
    }

    //判断是否收藏
    @Override
    public boolean isCollection(Integer userId, Integer postId) {
        String collectionKey="collection@Post:"+userId;
        if(!redisUtil.hasKey(collectionKey))
            return false;
        return redisUtil.sHasKey(collectionKey, postId);
    }

    //查看用户收藏列表
    @Override
    public List<Post> viewCollection(Integer userId) {
        String collectionKey="collection@Post:"+userId;
        if(!redisUtil.hasKey(collectionKey))
            return null;
        Set<Object> objects = redisUtil.sGet(collectionKey);
        List<Integer> postId=new ArrayList(objects);
        return postMapper.viewById(postId);
    }

    //点赞帖子
    @Override
    @Transactional
    public R postLike(Integer userId, Integer postId) {
        String likeKey="post@Like:"+postId;
        if(redisUtil.hasKey(likeKey)&&redisUtil.sHasKey(likeKey,userId)){
            redisUtil.setRemove(likeKey,userId);
            return new R(true,"取消点赞");
        }
        redisUtil.sSet(likeKey,userId);
        return new R(true,"点赞成功");
    }

    //查看是否点赞
    @Override
    public boolean postIsLike(Integer userId, Integer postId) {
        String likeKey="post@Like:"+postId;
        if(redisUtil.hasKey(likeKey)&&redisUtil.sHasKey(likeKey,userId))
            return true;
        return false;
    }

    //添加帖子类型
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addType(String type) {
        if(redisUtil.hasKey("postType")&&redisUtil.sHasKey("postType",type))
            return "该类型已经存在";
        redisUtil.sSet("postType",type);
        return null;
    }

    //删除帖子类型
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void typeDelete(String type) {
        redisUtil.setRemove("postType",type);
    }

    //修改帖子类型
    @Override
    public R updateType(String oldType, String newType) {
        if(redisUtil.sHasKey("postType",newType))
            return new R(false,"该类型已经存在");
        redisUtil.setRemove("postType",oldType);
        redisUtil.sSet("postType",newType);
        return new R(true,"修改成功");
    }

    //批量删除帖子类型
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void typeDeleteMore(String[] type) {
        for (String str:type) {
            redisUtil.setRemove("postType",str);
        }
    }

    //查看所有帖子类型
    @Override
    public Set<Object> typeView() {
        if(!redisUtil.hasKey("postType"))
            return null;
        return redisUtil.sGet("postType");
    }
}
