package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    private static final String PAGE_INDEX="role/index";
    private static final String PAGE_CREATE="role/create";
    private static final String PAGE_EDIT="role/edit";
    private static final String PAGE_ASSIGN_SHOW="role/assignShow";
    private static final String PAGE_SUCCESS="common/successPage";
    private static final String LIST_ACTION="redirect:/role";
    // @Autowired
    @DubboReference
     RoleService roleService;
    @DubboReference
    PermissionService permissionService;
    /*@RequestMapping
    public String index(Model model){
        List<Role> roleList = roleService.findAll();
        model.addAttribute("roleList",roleList);
        return PAGE_INDEX;//默认转发到页面
    }*/

    //分页查询
    @RequestMapping
    @PreAuthorize("hasAuthority('role.show')")
    public String index(Model model,HttpServletRequest request){

        //页面提交的参数(pageNum,pageSize,roleName)
        Map<String, Object> filters = getFilters(request);


        //分页查询
        PageInfo<Role> page = roleService.findPage(filters);
        model.addAttribute("page",page);



        model.addAttribute("filters",filters);
        return PAGE_INDEX;//默认转发到页面
    }






    //跳转到添加角色的页面
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/create")
    public String create(){
        return PAGE_CREATE;
    }
    //添加角色
    /*@PreAuthorize("hasAuthority('role.save')")*/
    @PreAuthorize("hasAnyAuthority('role.save','role.create1')")
    @RequestMapping("/save")
    public String save(Role role){
        roleService.insert(role);
        return PAGE_SUCCESS;
    }

    //跳转到修改的页面并回显
    @RequestMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId,Model model){
      Role role=  roleService.findById(roleId);
      model.addAttribute("role",role);
      return PAGE_EDIT;
    }
    //修改
    @RequestMapping("/update")
    public String update(Role role){
          roleService.update(role);
         return PAGE_SUCCESS;
    }

    //删除
    @RequestMapping("/delete/{roleId}")
    public String delete(@PathVariable("roleId") Long roleId){
         roleService.delete(roleId);
         return LIST_ACTION;
    }
    @RequestMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable("roleId") Long roleId,Model model){
        List<Map> mapList=permissionService.findPermissionByRoleId(roleId);
        model.addAttribute("roleId",roleId);
        model.addAttribute("zNodes",mapList);
        return PAGE_ASSIGN_SHOW;
    }

    //分配权限
    @RequestMapping("/assignPermission")
    public String assignPermission(Long roleId,Long[] permissionIds){
         permissionService.assignPermissionByRoleId(roleId,permissionIds);
        return PAGE_SUCCESS;
    }
}
