package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
   //@Autowired
    @Resource
   RoleDao roleDao;
    @Autowired
    AdminRoleDao adminRoleDao;
    @Override
    public BaseDao<Role> getBaseDao() {
        return roleDao;
    }

    @Override
    public Map findAdminRoleList(Long adminId) {
        //1.获取所有的角色信息
        List<Role> roleListAll = roleDao.findAll();
        //2.根据adminId获取拥有的角色id
       List<Long> roleIdList= adminRoleDao.findRoleIdByAdminId(adminId);
        //3.分类
        List<Role> assignRoleList=new ArrayList<>();
        List<Role> noAssignRoleList=new ArrayList<>();

        for (Role role : roleListAll) {
            if(roleIdList.contains(role.getId())){
                assignRoleList.add(role);
            }else{
                noAssignRoleList.add(role);
            }
        }

        Map map=new HashMap();
        map.put("assignRoleList",assignRoleList);
        map.put("noAssignRoleList",noAssignRoleList);

        return map;
    }

    //为用户分配角色
    @Override
    public void assignAdminRole(Long adminId, Long[] roleIds) {

         //1.将用户原来的角色分配信息删除
         adminRoleDao.deleteByAdminId(adminId);
        //2.重新分配
         /*
         * adminId  roleId
         *  1        1
         * 1         2
         * 1         3
         * */
      /*  for (Long roleId : roleIds) {
             if(StringUtils.isEmpty(roleId)){continue;}
            AdminRole adminRole=new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleDao.insert(adminRole);
        }*/
        adminRoleDao.insertBatch(adminId,roleIds);

    }
}
