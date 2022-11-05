package com.xingchen.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.PostCommentMapper;
import com.xingchen.dao.PostMapper;
import com.xingchen.dao.UserMapper;
import com.xingchen.dao.VipMapper;
import com.xingchen.pojo.*;
import com.xingchen.service.UserService;
import com.xingchen.utils.*;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VipMapper vipMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostCommentMapper postCommentMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DeleteUtils deleteUtil;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Value("${jwt.header}")
    private String tokenHeader;

    //账号注册
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R register(String userAccount,String userPassword){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(userMapper.selectOne(queryWrapper.eq("user_account",userAccount))!=null) {
            return new R(false, "该账号已经存在");
        }
        Random random=new Random(10);
        StringBuilder userName= new StringBuilder("T部落");
        for (int i=0;i<6;i++){
            int rand=random.nextInt(10);
            userName.append(rand);
        }
        userName.append("号");
        User user=new User(userAccount,userName.toString(),userPassword,null,"");
        user.setUserHeadshotUrl("https://lmy-1311156074.cos.ap-nanjing.myqcloud.com/R-C.jpg");
        userMapper.insert(user);
        return new R(true,"注册成功");
    }

    //发送邮件
    @Override
    public R sendEmail(String email) {
        try {
            String key="email@limit:"+email;
            String emailKey="email:"+email+"@value";
//            if(redisUtil.hasKey(key)&&redisUtil.sHasKey(key,emailKey))
//                return new R(false,"该用户一分钟内已经发送过一次验证码");
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("T部落验证码");
            Random random = new Random();
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int rand = random.nextInt(10);
                code.append(rand);
            }
            simpleMailMessage.setText("尊贵的T部落用户，您的验证码为：" + code + "，该验证码三分钟内有效，请勿泄露于他人");
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(email);
            mailSender.send(simpleMailMessage);
            String codeKey=email+":code";
            redisUtil.set(codeKey, code.toString(),180);
            redisUtil.sSetAndTime(key,60,emailKey);
            return new R(true,"发送成功");
        }
        catch (Exception e){
//            e.printStackTrace();
            return new R(false,"发送失败,请检查邮箱");
        }
    }

    //邮箱注册
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R registerByEmail(String email, String userPassword, String code) {
        String codeKey=email+":code";
        Object value = redisUtil.get(codeKey);
        if(!code.equals(value)){
            return new R(false,"验证码错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(userMapper.selectOne(queryWrapper.eq("user_account",email))!=null){
            return new R(false,"该邮箱已经注册过账号");
        }if(userMapper.selectOne(queryWrapper.eq("user_email",email))!=null){
            return new R(false,"该邮箱已经绑定过账号");
        }
        Random random=new Random(10);
        StringBuilder userName= new StringBuilder("T部落");
        for (int i=0;i<6;i++){
            int rand=random.nextInt(10);
            userName.append(rand);
        }
        userName.append("号");
        User user=new User(email,userName.toString(),userPassword,email,"");
        user.setUserIntroduce("");
        user.setUserHeadshotUrl("https://lmy-1311156074.cos.ap-nanjing.myqcloud.com/R-C.jpg");
        userMapper.insert(user);
        redisUtil.del(codeKey);
        return new R(true,"注册成功");
    }

    void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    //密码登录
    @Override
    public R login(String userAccount, String userPassword) {
    QueryWrapper<User> queryWrapper=new QueryWrapper<>();
    User user = userMapper.selectOne(queryWrapper.eq("user_account", userAccount).eq("user_password",userPassword));
    if(user==null)
        return new R(false,"密码错误");
    String key="userLimit";
    String userKey="user@"+user.getUserId();
    if(redisUtil.hasKey(key)&&redisUtil.sHasKey(key,userKey))
        return new R(false,"该用户已经被封禁");
    try {
        authenticate(userAccount,userPassword);
    } catch (Exception e) {
        e.printStackTrace();
    }
    final UserDetails userDetails = userDetailsService
            .loadUserByUsername(userAccount);
    final String token = jwtTokenUtil.generateToken(userDetails);
    return new R(true, ResponseEntity.ok(new JwtResponse(token)),"登录成功");
    }

    //验证码登录
    @Override
    public R loginByCode(String email, String code) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        User user = userMapper.selectOne(queryWrapper.eq("user_email", email));
        if(user==null){
            return new R(false,"该邮箱没有注册过账号");
        }
        if(user.getUserAuthority()==1){
            return new R(false,"请检查账号");
        }
        String key="userLimit";
        String userKey="user@"+user.getUserId();
        if(redisUtil.hasKey(key)&&redisUtil.sHasKey(key,"\""+userKey+"\""))
            return new R(false,"该用户已经被封禁");
        String codeKey=email+":code";
        Object value = redisUtil.get(codeKey);
        if(!code.equals(value)){
            return new R(false,"验证码错误");
        }
        try {
            authenticate(user.getUserAccount(),user.getUserPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user.getUserAccount());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new R(true,token,"登录成功");
    }

    //验证登录
    @Override
    public JwtUser isLogin( HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
//        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(username);
        DecodedJWT decode = JWT.decode(token);
        Claim id = decode.getClaim("id");
        Claim account = decode.getClaim("account");
        Claim password = decode.getClaim("password");
        Claim auth=decode.getClaim("authorities");
        return new JwtUser(id.asInt(),account.asString(),password.asString(),auth.asMap().get("authority").toString(),true);
    }

    //退出登录
    @Override
    public void quit(HttpServletRequest request) {
        String token =request.getHeader("token");
        String userLimitKey="user@Limit:Token";
        String tokenKey="token@Limit"+token;
        redisUtil.sSetAndTime(userLimitKey,5 * 60 * 60,tokenKey);
    }

    //删除单个用户
    @Override
    @Transactional
    public void delete(Integer userId) {
        String oldUrl=userMapper.selectById(userId).getUserHeadshotUrl();
        if(oldUrl!=null&&!oldUrl.equals("")&&!oldUrl.equals("https://lmy-1311156074.cos.ap-nanjing.myqcloud.com/R-C.jpg"))
            CosUtils.deleteObject(oldUrl);
        deleteUtil.userDelete(userId);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        List<Post> list = postMapper.selectList(queryWrapper);
        for(Post post:list){
            deleteUtil.postDelete(post.getPostId());
        }
        postCommentMapper.userPostDelete(userId);
        postMapper.delete(queryWrapper);
        userMapper.deleteById(userId);
    }

    //批量删除用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMore(Integer[] id) {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(id));
        for(User user:users){
            String oldUrl=user.getUserHeadshotUrl();
            if(oldUrl!=null&&!oldUrl.equals("")&&!oldUrl.equals("https://lmy-1311156074.cos.ap-nanjing.myqcloud.com/R-C.jpg"))
                CosUtils.deleteObject(oldUrl);
            deleteUtil.userDelete(user.getUserId());
            QueryWrapper queryWrapper=new QueryWrapper();
            List<Post> list = postMapper.selectList(queryWrapper);
            for(Post post:list){
                deleteUtil.postDelete(post.getPostId());
            }
            queryWrapper.eq("user_id",user.getUserId());
            postMapper.delete(queryWrapper);
        }
        userMapper.deleteBatchIds(Arrays.asList(id));
    }

    //修改用户信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(Integer userId,String name,String sex, String birthday, String introduce, String address, String school) {
        if(userId==null||name==null||sex==null||birthday==null||introduce==null||address==null)
            return new R(false,"不能为空值");
        User user=new User(userId,name,sex,birthday,introduce,address,school);
        if(userMapper.updateById(user)>0)
            return new R(true,"修改成功");
        return new R(false,"未修改");
    }

    //修改用户头像
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateHeadShot(Integer userId, MultipartFile file) {
        String oldUrl=userMapper.selectById(userId).getUserHeadshotUrl();
        if(oldUrl!=null&&!oldUrl.equals("")&&!oldUrl.equals("https://lmy-1311156074.cos.ap-nanjing.myqcloud.com/R-C.jpg"))
            CosUtils.deleteObject(oldUrl);
        String url=CosUtils.uploadCos("jpg,png,gif",file);
        System.out.println(url);
        if(url.equals("文件类型错误"))
            return new R(false,"文件类型错误");
        User user=new User(userId);
        user.setUserHeadshotUrl(url);
        userMapper.updateById(user);
        return new R(true,"修改成功");
    }

    //查看单个用户信息
    @Override
    public User viewOne(Integer userId) {
        User user = userMapper.selectById(userId);
        user.setFollow ((int)redisUtil.sGetSetSize("follow:" + userId));
        user.setFan((int)redisUtil.sGetSetSize("fan:" + userId));
        return user;
    }

    //分页查看用户信息
    @Override
    public IPage<User> viewByPage(Integer currentPage, Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_authority",0);
        IPage<User> page=new Page<>(currentPage,pageSize);
        userMapper.selectPage(page, queryWrapper);
        if (currentPage<1||pageSize<1) {
            return null;
        }
        if(currentPage > page.getPages()&&page.getPages()!=0){
            page.setCurrent(page.getPages());
            userMapper.selectPage(page, queryWrapper);
        }
        return page;
    }

    //分页搜索
    @Override
    public IPage<User> searchByPage(Integer currentPage, Integer pageSize, String search) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_authority",0);
        queryWrapper.like("user_account",search).or().like("user_name",search);
        IPage<User> page=new Page<>(currentPage,pageSize);
        userMapper.selectPage(page, queryWrapper);
        if (currentPage<1||pageSize<1) {
            return null;
        }
        if(currentPage > page.getPages()&&page.getPages()!=0){
            page.setCurrent(page.getPages());
            userMapper.selectPage(page, queryWrapper);
        }
        return page;
    }

    //关注用户（取关）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R follow(Integer followId, Integer fanId) {
        String followKey="fan:"+followId;
        String fanKey="follow:"+fanId;
        String msg="";
        if(redisUtil.sHasKey(followKey,fanId)){
            redisUtil.setRemove(followKey,fanId);
            redisUtil.setRemove(fanKey,followId);
            msg="取消关注";
        }
        else {
            redisUtil.sSet(followKey,fanId);
            redisUtil.sSet(fanKey,followId);
            msg="关注成功";
        }
        return new R(true,msg);
    }

    //判断用户是否关注
    @Override
    public boolean isFollow(Integer followId, Integer fanId) {
        String fanKey="follow:"+fanId;
        if(redisUtil.hasKey(fanKey)&&redisUtil.sHasKey(fanKey,followId))
            return true;
        return false;
    }

    //查看用户关注
    @Override
    public List<User> viewFollow(Integer userId) {
        String followKey="follow:"+userId;
        Set<Object> followId = redisUtil.sGet(followKey);
        List<Integer> id=new ArrayList(followId);
        if(id.size()==0)
            return null;
        return userMapper.selectBatchIds(id);
    }

    //查看用户粉丝
    @Override
    public List<User> viewFan(Integer userId) {
        String fanKey="fan:"+userId;
        Set<Object> fanId = redisUtil.sGet(fanKey);
        List<Integer> id=new ArrayList(fanId);
        if(id.size()==0)
            return null;
        return userMapper.selectBatchIds(id);
    }

    //设置vip
    @Override
    public void setVip(Integer userId, Double price) {
        QueryWrapper<Vip> queryWrapper=new QueryWrapper<>();
        Vip vip = vipMapper.selectOne(queryWrapper.eq("vip_price", price));
        Integer vipTime = vip.getVipTime();
        User user = userMapper.selectById(userId);
        user.setUserVip(1);
        userMapper.updateById(user);
        user.setUserVip(0);
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        service.schedule(new Runnable() {
            @Override
            public void run() {
                userMapper.updateById(user);
            }
        },vipTime*30, TimeUnit.DAYS);
    }

    //封禁用户
    @Override
    public R userLimit(Integer userId, Integer day) {
        String key="userLimit";
        String userKey="user@"+userId;
        if(redisUtil.hasKey(key)&&redisUtil.sHasKey(key,userKey)){
            redisUtil.setRemove(key,userKey);
            return new R(true,"解封成功");
        }
        redisUtil.sSetAndTime(key,day*24*60*60,userKey);
        return new R(true,"封禁成功");
    }

    //判断是否封禁
    @Override
    public boolean userIsLimit(Integer userId) {
        String key="userLimit";
        String userKey="user@"+userId;
        if(redisUtil.hasKey(key)&&redisUtil.sHasKey(key,userKey))
            return true;
        return false;
    }

    //发送邮件
    @Override
    public R sendEmailSelf(Integer userId, String content) {
        User user = userMapper.selectById(userId);
        if(user.getUserEmail()==null||user.getUserEmail().equals("")){
            return new R(false,"该用户没有邮箱");
        }
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("T部落");
            simpleMailMessage.setText(content);
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(user.getUserEmail());
            mailSender.send(simpleMailMessage);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return new R(true,"发送成功");
    }
}
