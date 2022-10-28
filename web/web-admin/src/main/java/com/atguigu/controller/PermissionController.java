package com.atguigu.controller;

import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController {
     @DubboReference
    PermissionService permissionService;
    private static final String PAGE_INDEX = "permission/index";
    private static final String PAGE_CREATE = "permission/create";
    private static final String PAGE_EDIT = "permission/edit";
    private static final String LIST_ACTION = "redirect:/permission";
    private static final String PAGE_SUCCESS = "common/successPage";
    @RequestMapping
    public String index(Model model){
        List<Permission> permissionList = permissionService.findMenuByAdminId(1l);
        model.addAttribute("list",permissionList);
        return PAGE_INDEX;
    }
    //跳转页面
    @RequestMapping("/create")
    public String create(Permission permission,Model model){
      model.addAttribute("permission",permission);
      return PAGE_CREATE;
    }
   /* public String create(Long parentId,Integer type,String parentName,Model model){

    }*/


    @RequestMapping("/save")
    public String save(Permission permission){
       permissionService.insert(permission);
        return PAGE_SUCCESS;
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Permission permission = permissionService.findById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(Permission permission){
        permissionService.update(permission);
        return PAGE_SUCCESS;
    }

}
