package com.atguigu.controller;

import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
       @Autowired
       JedisPool jedisPool;
      @DubboReference
    UserInfoService userInfoService;
    //发送验证码
    @RequestMapping("/sendCode/{phoneNo}")
    public Result sendCode(@PathVariable("phoneNo")String phoneNo, HttpServletRequest request) throws Exception {
         //String code="1111";
         //服务器临时保存验证码
         //request.getSession().setAttribute("CODE",code);
        //动态生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //发送验证码
        SMSUtils.sendShortMessage(phoneNo,code.toString());
        //redis保存验证码
        Jedis jedis = jedisPool.getResource();
        String codeKey=phoneNo+":code";
        jedis.setex(codeKey,60*60*24,code.toString());
        return Result.ok(code);
    }
    //注册
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo,HttpServletRequest request){
         //获取封装对象registerVo中的属性
        String nickName = registerVo.getNickName();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        String phone = registerVo.getPhone();

        //非空验证
      /*  if(nickName==null||!nickName.equals("")){

        }*/
        if(
                StringUtils.isEmpty(nickName)||
                StringUtils.isEmpty(code)||
                StringUtils.isEmpty(password)||
                StringUtils.isEmpty(phone)

        ){
            return Result.build(null,ResultCodeEnum.PARAM_ERROR);
        }
       //判断验证码输入是否正确
       // Object codedb = request.getSession().getAttribute("CODE");
        String codedb = jedisPool.getResource().get(phone + ":code");
        if(codedb==null||!code.equals(codedb)){
            return Result.build(null,ResultCodeEnum.CODE_ERROR);
        }

        //用户是否已经注册 (条件phone)
        UserInfo userInfo=userInfoService.findUserByPhone(phone);
        if(userInfo!=null){
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }


        //保存userInfo对象
         userInfo=new UserInfo();
         userInfo.setNickName(nickName);
         userInfo.setPassword(MD5.encrypt(password));
         userInfo.setPhone(phone);
         userInfo.setStatus(1);
        userInfoService.insert(userInfo);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //登录
    @RequestMapping("/login")
    public Result login(@RequestBody LoginVo loginVo,HttpServletRequest request){
        String phone = loginVo.getPhone();
        String password = loginVo.getPassword();

        //非空验证
        if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
            return Result.build(null,ResultCodeEnum.PARAM_ERROR);
        }
        //登录(手机号和密码作为条件查询用户信息)
        //登录 先使用手机号把用户查询出来 再比较密码
        UserInfo userInfo = userInfoService.findUserByPhone(phone);
        if(userInfo==null){
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }

        //比较密码
        if(!userInfo.getPassword().equals(MD5.encrypt(password))){
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }

        //该账号是否被锁定
        if(userInfo.getStatus()==0){
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }

  //把登录成功的用户信息保存到session中
        request.getSession().setAttribute("USER",userInfo);


        Map map=new HashMap();
        map.put("nickName",userInfo.getNickName());
        return Result.ok(map);
    }
}
