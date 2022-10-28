package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtil;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    //@Autowired
    @DubboReference
    AdminService adminService;
    @DubboReference
    RoleService roleService;
    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_CREATE = "admin/create";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";
    private static final String LIST_ACTION = "redirect:/admin";
    private static final String PAGE_SUCCESS = "common/successPage";

    @RequestMapping
    public String index(Model model, HttpServletRequest request) {
        //账号名字userName name姓名  phone  createTimeBegin  createTimeEnd
        Map<String, Object> filters = getFilters(request);
        PageInfo<Admin> page = adminService.findPage(filters);
        model.addAttribute("filters", filters);
        model.addAttribute("page", page);
        return PAGE_INDEX;
    }

    //跳转到create页面
    @RequestMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    @RequestMapping("/save")
    public String save(Admin admin) {
        admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
        adminService.insert(admin);
        return PAGE_SUCCESS;
    }

    //回显
    @RequestMapping("/edit/{adminId}")
    public String edit(@PathVariable("adminId") Long adminId, Model model) {
        Admin admin = adminService.findById(adminId);
        model.addAttribute("admin", admin);
        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(Admin admin) {
        adminService.update(admin);
        return PAGE_SUCCESS;
    }


    @RequestMapping("/delete/{adminId}")
    public String delete(@PathVariable("adminId") Long adminId) {
        adminService.delete(adminId);
        return LIST_ACTION;
    }

    //跳转到上传页面
    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "admin/upload";
    }

    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        //1.七牛云上传图片
        String filename = multipartFile.getOriginalFilename();
        String subName = filename.substring(filename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + subName;
        QiniuUtil.upload2Qiniu(multipartFile.getBytes(), newFileName);
        //2.修改用户的头像
        String url = "http://rk4v93v4p.hd-bkt.clouddn.com/" + newFileName;
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(url);
        adminService.update(admin);

        return PAGE_SUCCESS;
    }


    //跳转到分配角色的页面
    @RequestMapping("/assignShow/{adminId}")
    public String assignShow(@PathVariable("adminId") Long adminId, Model model) {
        model.addAttribute("adminId", adminId);
        /*
         *  assginRoleList:list
         *  noAssginRoleList:list
         * */
        Map map = roleService.findAdminRoleList(adminId);
        model.addAllAttributes(map);
        return PAGE_ASSIGN_SHOW;
    }

    /*
      roleIds: 1,2,3,4,5,
    * */
    @RequestMapping("/assignRole")
    public String assignRole(Long adminId, Long[] roleIds) {
        roleService.assignAdminRole(adminId, roleIds);
        return PAGE_SUCCESS;
    }


}
