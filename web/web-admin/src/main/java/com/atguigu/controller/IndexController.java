package com.atguigu.controller;

import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {
    @DubboReference
    AdminService adminService;
    @DubboReference
    PermissionService permissionService;
    private static final String PAGE_INDEX = "frame/index";
    private static final String PAGE_MAIN = "frame/main";
    private static final String PAGE_LOGIN = "frame/login";
    private static final String PAGE_AUTH = "frame/auth";

    @RequestMapping("/")
    public String index(Model model) {
        //admin 登录的用户
        //假设系统管理员
        // Long adminId=1l;
        //从springsecurity 中获取登录成功的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        //Admin admin = adminService.findById(adminId);
        Admin admin = adminService.findAdminByUserName(user.getUsername());
        model.addAttribute("admin", admin);
        //登录用户的权限(具体查询权限表中的菜单 type=1)
        List<Permission> permissionList = permissionService.findMenuByAdminId(admin.getId());
        model.addAttribute("permissionList", permissionList);
        return PAGE_INDEX;
    }

    @RequestMapping("/main")
    public String main() {
        return PAGE_MAIN;
    }

    @RequestMapping("/login")
    public String login() {

        return PAGE_LOGIN;
    }
    @RequestMapping("/auth")
    public String auth(){
        return PAGE_AUTH;
    }


}
