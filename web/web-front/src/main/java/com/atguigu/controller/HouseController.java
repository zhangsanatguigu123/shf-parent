package com.atguigu.controller;

import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/house")
public class HouseController {
     @DubboReference
    HouseService houseService;
     @DubboReference
    CommuntyService communtyService;
     @DubboReference
    HouseBrokerService houseBrokerService;
     @DubboReference
    HouseImageService houseImageService;
     @DubboReference
    UserFollowService userFollowService;
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result list(
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize,
            @RequestBody HouseQueryVo houseQueryVo
            ){
        PageInfo<HouseVo> pageInfo= houseService.findListByPage(pageNum,pageSize,houseQueryVo);
        return Result.ok(pageInfo);
    }

    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Long houseId, HttpServletRequest request){
        House house = houseService.findById(houseId);
        Community community = communtyService.findById(house.getCommunityId());
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(houseId);
        List<HouseImage> houseImage1List = houseImageService.findListByHouseId(houseId, 1);
        Map map=new HashMap();
        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImage1List);

        //根据 登录的用户id(userId) 和当前房源id(houseId) 查询 follow表
       UserInfo userInfo=(UserInfo) request.getSession().getAttribute("USER");
           if(userInfo==null){
               map.put("isFollow",false);
               return Result.ok(map);
           }
        Long userId = userInfo.getId();
        boolean result= userFollowService.findFollowBy(userId,houseId);
        map.put("isFollow",result);
        return Result.ok(map);
    }
}
