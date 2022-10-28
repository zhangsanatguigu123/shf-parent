package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration  //配置类： 作用等价于spring.xml
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    CustomAccessDeineHandler customAccessDeineHandler;

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // super.configure(auth);  禁用调用父类中的认证方式
        //内存中指定登录的用户信息(模拟的是数据库中的用户)
        // auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("");
        // auth.inMemoryAuthentication().withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("");
        //  auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder.encode("admin")).roles("");
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }


    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http); // 默认调用父类中的方法 所有资源都被限制
        http.headers().frameOptions().sameOrigin();//页面中的iframe标签放行

        //1.匿名访问的资源.antMatchers("/static/**","/login")
        //.permitAll().anyRequest().authenticated(); 其它的请求必须登录
        http.authorizeRequests()
                .antMatchers("/static/**", "/login")
                .permitAll().anyRequest().authenticated();


        //指定登录信息
        http.formLogin().loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password").defaultSuccessUrl("/").failureUrl("/login");

        //注册403处理类
        http.exceptionHandling().accessDeniedHandler(customAccessDeineHandler);

        //退出
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true);
        //禁用_csrf
        http.csrf().disable();
    }

    //密码加密器
    @Bean  //<bean id="" class=""> 创建bean对象并再IOC容器中管理
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
