package com.atguigu.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    //请求到达控制器方法前,进行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo userInfo =(UserInfo) request.getSession().getAttribute("USER");
        if(userInfo==null){
            Result result=new Result();
            result.setCode(208);
            String resultJson = JSON.toJSONString(result);
            response.getWriter().write(resultJson);
             return false;
        }
        return true;
    }
}
