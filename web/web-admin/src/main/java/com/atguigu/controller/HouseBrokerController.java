package com.atguigu.controller;

import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {
     @DubboReference
    AdminService adminService;
     @DubboReference
    HouseBrokerService houseBrokerService;
     private static final String PAGE_CREATE="houseBroker/create";
     private static final String PAGE_EDIT="houseBroker/edit";
     private static final String PAGE_SUCCESS="common/successPage";
     private static final String PAGE_ERROR="common/errorPage";
    @RequestMapping("/create/{houseId}")
    public String create(@PathVariable("houseId") Long houseId, Model model){
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("houseId",houseId);
        model.addAttribute("adminList",adminList);
        return PAGE_CREATE;
    }
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker,Model model){
         //判断当前房源是否已经分配了该经纪人
         HouseBroker houseBrokerdb= houseBrokerService.findBrokerByHouseIdAndBrokerId(houseBroker);
         if(houseBrokerdb!=null){
              model.addAttribute("info","当前房源已经分配了该经纪人");
             return PAGE_ERROR;
         }


         //根据brokerId 获取admin中的管理员信息
        Admin admin = adminService.findById(houseBroker.getBrokerId());
        //为经纪人的名字和头像赋值
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
       houseBrokerService.insert(houseBroker);
       return PAGE_SUCCESS;
    }

    //回显
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        HouseBroker houseBroker = houseBrokerService.findById(id);
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList",adminList);
        model.addAttribute("houseBroker",houseBroker);
        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(HouseBroker houseBroker){
        //根据brokerId 获取admin中的管理员信息
        Admin admin = adminService.findById(houseBroker.getBrokerId());
        //为经纪人的名字和头像赋值
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
         houseBrokerService.update(houseBroker);
         return PAGE_SUCCESS;
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseBrokerService.delete(id);
        return "redirect:/house/"+houseId;
    }

}
