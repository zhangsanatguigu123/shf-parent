package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    PermissionDao permissionDao;
    @Autowired
    RolePermissionDao rolePermissionDao;
    @Override
    public BaseDao<Permission> getBaseDao() {
        return permissionDao;
    }
    /*
    *  { id:221, pId:22, name:"随意勾选 2-2-1", checked:true},
    * */
    @Override
    public List<Map> findPermissionByRoleId(Long roleId) {
        List<Map> mapList=new ArrayList<>();

        //1.查询所有的权限
        List<Permission> permissionListAll = permissionDao.findAll();
        //2.查询角色拥有的权限id
        List<Long> permissionIdList=  rolePermissionDao.findPermissionIdByRoleId(roleId);
        //3.分类
        for (Permission permission : permissionListAll) {
            Map map=new HashMap();
            map.put("id",permission.getId());
            map.put("pId",permission.getParentId());
            map.put("name",permission.getName());
            if(permissionIdList.contains(permission.getId())){
                map.put("checked",true);
            }
            mapList.add(map);

        }


        return mapList;
    }

    @Override
    public void assignPermissionByRoleId(Long roleId,Long[] permissionIds) {
        //1.删除原有的角色和权限的关系
          rolePermissionDao.deleteByRoleId(roleId);
        //2.重新添加新的关系
         rolePermissionDao.insetBatch(roleId,permissionIds);
    }

    @Override
    public List<Permission> findMenuByAdminId(Long adminId) {
        List<Permission> permissionList =null;
         if(adminId==1){
             permissionList=permissionDao.findAll();
         }else{
             permissionList=permissionDao.findPermissionByAdminId(adminId);
         }
         //把集合中的数据按照父子关系保存
        List<Permission> TreeNodesList  = PermissionHelper.bulid(permissionList);
        return TreeNodesList;
    }
}
