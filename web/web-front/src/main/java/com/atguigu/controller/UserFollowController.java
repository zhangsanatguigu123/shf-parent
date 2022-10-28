package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/userFollow")
@RestController
public class UserFollowController extends BaseController {
    @DubboReference
    UserFollowService userFollowService;
    //关注房源
    @GetMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpServletRequest request){
        //获取已经登录成功的用户信息
        UserInfo userInfo =(UserInfo) request.getSession().getAttribute("USER");
        Long userId = userInfo.getId();
        //向follow表添加数据(userId,houseId)
        UserFollow userFollow=new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setHouseId(houseId);
        userFollowService.insert(userFollow);
        return Result.ok();
    }

    //以分页的形式查询(登录的用户)关注的房源信息
    @RequestMapping("/auth/list/{pageNum}/{pageSize}")
    public Result list(
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize,
            HttpSession session
    ){
    //获取登录的用户信息
        UserInfo userInfo =(UserInfo) session.getAttribute("USER");
        Long userId = userInfo.getId();
        PageInfo<UserFollowVo> pageInfo= userFollowService.ListPage(userId,pageNum,pageSize);

        return Result.ok(pageInfo);
    }

    //取消关注
    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){
          userFollowService.delete(id);
        return Result.ok();
    }

}
