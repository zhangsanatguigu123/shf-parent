package com.atguigu.config;

import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @DubboReference
    AdminService adminService;
    //username 客户端的用户名字
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库根据用户名字查询用户信息
        Admin admin= adminService.findAdminByUserName(username);
        if(admin==null){throw  new UsernameNotFoundException("用户不存在!!");}

        //权限集合
        List<GrantedAuthority> authorities=new ArrayList<>();
        //authorities.add(new SimpleGrantedAuthority("admin.create"));
        //权限的code (理解为具体的权限)
        List<String> codeList= adminService.findPermissionCodeByAdminId(admin.getId());

        for (String code : codeList) {
             authorities.add(new SimpleGrantedAuthority(code));
        }

        return new User(admin.getUsername(),admin.getPassword(), authorities);
        /*return new User(admin.getUsername(),admin.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));*/
    }
}
