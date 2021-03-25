package com.baizhi.controller;

import com.alibaba.druid.util.StringUtils;
import com.baizhi.entity.Favorite;
import com.baizhi.entity.Played;
import com.baizhi.entity.User;
import com.baizhi.entity.Video;
import com.baizhi.feignclients.VideosClient;
import com.baizhi.service.FavoriteService;
import com.baizhi.service.PlayedService;
import com.baizhi.service.UserService;
import com.baizhi.utils.*;
import com.baizhi.vo.MsgVO;
import com.baizhi.vo.VideoVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);



    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private VideosClient videosClient;

    @Autowired
    private PlayedService playedService;

    @Autowired
    private FavoriteService favoriteService;


    /**
     * 用户播放历史列表
     */
    @GetMapping("/user/played")
    public List<VideoVO> played(HttpServletRequest request,@RequestParam(value = "page",defaultValue = "1") Integer page,@RequestParam(value = "per_page",defaultValue = "5") Integer rows){
        log.info("当前页: {} 每页显示记录: {}",page,rows);
        User user = getUser(request);
        if(user==null)throw new RuntimeException("提示: 无效Token!");
        List<VideoVO> videoVOS = playedService.queryByUserId(user.getId(), page, rows);
        log.info("当前用户播放历史的视频为: {}",JSONUtils.writeValueAsString(videoVOS));
        return videoVOS;
    }

    /**
     * 用户收藏列表
     */
    @GetMapping("/user/favorites")
    public List<VideoVO> favorites(HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        List<VideoVO> videoVOS = favoriteService.findFavoritesByUserId(user.getId());
        log.info("当前用户收藏的视频为: {}",JSONUtils.writeValueAsString(videoVOS));
        return videoVOS;
    }

    /**
     * 用户取消收藏视频
     */
    @DeleteMapping("/user/favorites/{id}")
    public void cancelFavorites(@PathVariable("id") Integer videoId,HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        log.info("取消收藏的视频id: {}",videoId);
        int i = favoriteService.deleteByVideoIdAndUserId(videoId, user.getId());
        log.info("取消视频收藏成功:, {}",i>0);

    }

    /**
     * 用户收藏视频
     */
    @PutMapping("/user/favorites/{id}")
    public void createFavorites(@PathVariable("id") Integer videoId,HttpServletRequest request){
        log.info("收藏的视频id: {}",videoId);
        //1.获取当前登陆用户信息
        User user = (User) request.getAttribute("user");
        //2.判断是否收藏该视频
        Favorite favorite = favoriteService.queryByVideoIdAndUserId(videoId,user.getId());
        if(ObjectUtils.isEmpty(favorite)){
            favorite = new Favorite();
            favorite.setVideoId(videoId);
            favorite.setUid(user.getId());
            favorite = favoriteService.insert(favorite);
            log.info("收藏视频成功: {}",JSONUtils.writeValueAsString(favorite));
        }
    }


    /**
     * 用户取消不喜欢
     */
    @DeleteMapping("/user/disliked/{id}")
    public void cancelDisliked(@PathVariable("id") String videoId,HttpServletRequest request){
        //1.获取当前用户信息
        User user = (User) request.getAttribute("user");

        //2.将当前视频从用户不喜欢的列表中移除掉
        if(stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_DISLIKE_PREFIX+user.getId(),videoId)){
            stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_DISLIKE_PREFIX+user.getId(),videoId);
        }
    }

    /**
     *  视频点击不喜欢
     */
    @PutMapping("/user/disliked/{id}")
    public void disliked(@PathVariable("id") String videoId,HttpServletRequest request){
       // 1.获取当前点击用户的信息
        User user = (User) request.getAttribute("user");

        //2.放入当前用户不喜欢的列表
        stringRedisTemplate.opsForSet().add(RedisPrefix.USER_DISLIKE_PREFIX + user.getId(),videoId);

        //3.判断之前是否点击过点赞过该视频 如果点赞过该视频 将该视频从点赞列表中移除出来
        if(stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_LIKE_PREFIX+user.getId(),videoId)){
            stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_LIKE_PREFIX+user.getId(),videoId);//从喜欢中列表中删除
            stringRedisTemplate.opsForValue().decrement(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX+videoId);//当前视频喜欢次数-1
        }

    }


    /**
     * 视频播放
     */
    @PutMapping("/user/played/{id}")
    public void played(@PathVariable("id") String videoId,HttpServletRequest request){

        //redis中播放次数+1
        stringRedisTemplate.opsForValue().increment(RedisPrefix.VIDEO_PLAYED_COUNT_PREFIX +videoId);

        //1.获取登录用户
        User user = getUser(request);
        //2.没有登录用户不能存在播放记录
        if (!ObjectUtils.isEmpty(user)){
            //记录用户的播放历史
            Played played = new Played();
            played.setUid(user.getId());
            played.setVideoId(Integer.valueOf(videoId));
            played = playedService.insert(played);
            log.info("当前用户的播放记录保存成功,信息为: {}",JSONUtils.writeValueAsString(played));
        }
    }

    /**
     * 用户取消点赞视频  13  15
     */
    @DeleteMapping("/user/liked/{id}")
    public void cancelLiked(@PathVariable("id") Integer videoId,HttpServletRequest request){

        //1.获取用户信息
        User user = (User) request.getAttribute("user");
        log.info("接收的到视频id: {}",videoId);


        //2.将当前用户喜欢的列表中该视频移除掉
        stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_LIKE_PREFIX+user.getId(),videoId.toString());

        //3.将视频点赞次数-1
        stringRedisTemplate.opsForValue().decrement(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX+videoId);
    }


    /**
     * 用户点赞视频
     */
    @PutMapping("/user/liked/{id}")
    public void liked(@PathVariable("id") String videoId,HttpServletRequest request){
        //1.获取当前登陆用户信息
        User user = (User) request.getAttribute("user");
        log.info("接收的到视频id: {}",videoId);

        //2.将视频点赞次数+1                             //VIDEO_LIKE_COUNT_15  1
        stringRedisTemplate.opsForValue().increment(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX+videoId);


        //3.将当前用户点赞视频列表放入redis中//set模型   //USER_LIKE_13   [15]
        stringRedisTemplate.opsForSet().add(RedisPrefix.USER_LIKE_PREFIX +user.getId(),videoId);


        //4.判断当前用户是否曾经不喜欢过该视频,如果不喜欢该视频必须将该视频从不喜欢中移除出来
        if(stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_DISLIKE_PREFIX + user.getId(), videoId)){
            stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_DISLIKE_PREFIX + user.getId(),videoId);
        }


    }



    /**
     * 用户发布视频
     */
    @PostMapping("/user/videos")
    //MultipartFile file:用来接收上传视频信息
    //使用video对象接收 视频标题  视频简介  video{title,intro}
    //category_id 代表当前视频分类id
    //request:    当前请求上下文中存在用户信息
    public Video publishVideos(MultipartFile file, Video video, Integer category_id, HttpServletRequest request) throws IOException {

        //1.获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        log.info("接收文件名称: {}", originalFilename);
        log.info("接收到视频信息: "+new ObjectMapper().writeValueAsString(video));
        log.info("类别id: {}",category_id);
        log.info("文件大小为: {}",file.getSize());

        //2.获取文件后缀 mp4 avi ....
        String ext = FilenameUtils.getExtension(originalFilename);

        //3.生成uuid  32
        String uuidFileName = UUID.randomUUID().toString().replace("-", "");

        //4.生成uuid文件名名称
        String newFileName = uuidFileName + "." + ext;

        //5.上传阿里云oss 返回文件在oss地址
        String url = OSSUtils.upload(file.getInputStream(), "videos", newFileName);
        log.info("上传成功返回的地址: {}",url);

        //1.阿里云oss截取视频中某一帧作为封面   [推荐]   不存在
        String cover = url+"?x-oss-process=video/snapshot,t_30000,f_jpg,w_0,h_0,m_fast,ar_auto";
        log.info("阿里云oss根据url截取视频封面: {}",cover);

        //2.java工具类截取  实际存在
        /*InputStream inputStream = FileImageUtils.randomGrabberFFmpegImage(url);
        String cover = OSSUtils.upload(inputStream, "imgs", uuidFileName + ".jpg");
        log.info("上传封面地址为: {}",cover);*/

        //6.设置视频信息
        video.setCover(cover);//设置视频封面
        video.setLink(url);//设置视频地址
        video.setCategoryId(category_id);//设置类别id

        //获取用户信息
        User user = (User) request.getAttribute("user");
        video.setUid(user.getId());//设置发布用户id

        //调用视频服务
        Video videoResult = videosClient.publish(video);
        log.info("视频发布成功之后返回的视频信息: {}", JSONUtils.writeValueAsString(videoResult));
        return videoResult;
    }


    /**
     * 用户登录
     */
    @PostMapping("tokens")
    public Map<String,Object> tokens(@RequestBody MsgVO msgVO, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        //1.获取用户手机号
        String phone = msgVO.getPhone();
        //2.获取用户验证码
        String captcha = msgVO.getCaptcha();
        log.info("手机号: {},验证码:{}", phone, captcha);

        String phoneKey = "phone_"+phone;
        //3.根据手机号判断redis中是否还存在该手机号验证码,如果不存在说明验证码已经过期!
        if(!stringRedisTemplate.hasKey(phoneKey)) throw new RuntimeException("提示:验证码已经过期!");

        //4.根据手机号获取redis中验证码
        String redisCaptcha = stringRedisTemplate.opsForValue().get(phoneKey);

        //5.比较用户输入的验证码和redis中验证码是否一致
        if (!StringUtils.equals(captcha,redisCaptcha)) throw  new RuntimeException("提示:验证码输入错误!");

        //6.判断是否为首次登录  //判断手机号之前是否登录过
        User user = userService.findByPhone(phone);

        if(ObjectUtils.isEmpty(user)){
            user = new User();//创建一个用户对象
            user.setCreatedAt(new Date());//设置创建时间
            user.setUpdatedAt(new Date());//设置更新时间
            user.setPhone(phone); //设置用户的手机号
            user.setIntro("");//设置简介为空
            //初始化默认头像
            user.setAvatar(ImageUtils.getPhoto());//随机初始化头像
            user.setPhoneLinked(1);//是否绑定手机
            user.setWechatLinked(0);//是否绑定微信
            user.setFollowersCount(0);//设置粉丝数
            user.setFollowingCount(0);//设置关注数
            user = userService.insert(user);//保存用户信息
        }

        //7.保存用户登录标记
        String token = request.getSession().getId(); //根据请求sessionid  手机号
        String tokenKey = "session_"+ token; //session_xxxxx  session_132...
        log.info("生成token: {}",token);
        //redisTemplate 操作对象  key:对象  value:对象  注意:必须实现对象序列化接口  jdk序列化方式
        //redisTemplate对象中默认序列化方式进行修改: key:String序列化方式   value:json序列化
        redisTemplate.opsForValue().set(tokenKey,user,7, TimeUnit.DAYS);

        result.put("token",token);
        return result;
    }




    /**
     * 发送短信验证码
     * @param msgVO
     */
    @PostMapping("captchas")
    public void captchas(@RequestBody MsgVO msgVO){
        //1.获取接收到的手机号
        String phone = msgVO.getPhone();
        log.info("发送短信的手机号为: {}",phone);

        // 每次发送验证码之前判断,是否存在timeout_132... timeout_176
        String timeoutKey = "timeout_" + phone;
        if (stringRedisTemplate.hasKey(timeoutKey)) {
            throw  new  RuntimeException("提示: 不允许重复发送!");
        }
        try {
            //2.生成4位随机字符
            String code = RandomStringUtils.randomNumeric(4);

            //3.根据接收手机号以及生成随机字符 发送验证码
            SMSUtils.sendMsg(phone,code);

            //4.将验证码放入redis   key: phone_132....   value:code
            String phoneKey = "phone_"+phone;//给key加入一个前缀
            stringRedisTemplate.opsForValue().set(phoneKey,code,120, TimeUnit.MINUTES);//2分钟验证有效

            //5.如果验证码在有效期内,不允许重新发送  //timeout_132... true
            stringRedisTemplate.opsForValue().set(timeoutKey,"true",120,TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("提示: 短信发送失败!");
        }
    }



    /**
     * 已登录的用户信息
     */
    @GetMapping("user")
    public User user(HttpServletRequest request) throws JsonProcessingException {
        User user = (User) request.getAttribute("user");
        log.info("获取的用户信息为: {},", new ObjectMapper().writeValueAsString(user));
        return user;
    }


    /**
     * 修改用户信息
     */
    @PatchMapping("user")         // user  手机号  验证码   name  intro
    public User user(@RequestBody User user,HttpServletRequest request){
        //1.获取token
        String token = (String) request.getAttribute("token");
        //2.获取原始用户对象
        User userOld = (User) request.getAttribute("user"); //所有字段都有
        //是否修改手机号
        if(!StringUtils.isEmpty(user.getPhone())){
            //1.根据用户本次修改手机号
            String phoneKey = "phone_"+user.getPhone();
            //2.判断当前手机号是否在redis中存在当前key
            if(!stringRedisTemplate.hasKey(phoneKey)) throw new RuntimeException("提示:验证码已过期!");
            String redisCaptcha = stringRedisTemplate.opsForValue().get(phoneKey);//获取redis验证码
            if(!StringUtils.equals(redisCaptcha,user.getCaptcha())) throw new RuntimeException("提示:验证码输入错误!");
            //3.修改手机号
            userOld.setPhone(user.getPhone());
        }
        //判断是否修改姓名
        if(!StringUtils.isEmpty(user.getName())) userOld.setName(user.getName());
        //判断是否修改简介
        if(!StringUtils.isEmpty(user.getIntro()))userOld.setIntro(user.getIntro());
        //执行更新
        User u = userService.update(userOld);
        //放入redis
        redisTemplate.opsForValue().set("session_"+token,u,7, TimeUnit.DAYS);
        return u;
    }


    /**
     * 注销登录
     */
    @DeleteMapping("tokens")
    public void logout(String token){
        log.info("当前获获取的token信息: {}",token);
        //1.根据接收token拼接对应TokenKey
        String tokenKey = "session_" + token;
        //2.根据TokenKey在redis中删除
        stringRedisTemplate.delete(tokenKey);
    }


    /**
     * 测试服务
     * @return
     */
    @GetMapping("demo")
    public String demo(){
        log.info("user demo is ok!!!");
        return "user ok !";
    }



    public User getUser(HttpServletRequest request){
        String token = request.getParameter("token");
        log.info("token为: {}",token);
        String tokenKey = "session_" + token;
        return (User) redisTemplate.opsForValue().get(tokenKey);
    }
}