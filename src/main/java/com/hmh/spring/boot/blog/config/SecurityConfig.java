package com.hmh.spring.boot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ EnableGlobalMethodSecurity(prePostEnabled = true) 启用方法安全设置（使得@PreAuthorize可以生效）
 * 安全配置类（包含认证和授权）
 * （1）认证是由 AuthenticationManager 来管理的，但是真正进行认证的是 AuthenticationManager 中定义的 AuthenticationProvider。
 * AuthenticationManager 中可以定义有多个 AuthenticationProvider。当我们使用 authentication-provider 元素来定义一个
 * AuthenticationProvider 时，如果没有指定对应关联的 AuthenticationProvider 对象，Spring Security 默认会使用
 * DaoAuthenticationProvider。DaoAuthenticationProvider 在进行认证的时候需要一个 UserDetailsService 来获取用户的信息 UserDetails
 * （2）授权由http.authorizeRequests().antMatchers来完成，结合UserDetails的权限
 *
 * @author hmh
 * @date 2019/3/7
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 加密token的密钥
     */
    private static final String KEY = "humh.com/blogs";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 装配passwordEncoder
     *
     * @author hmh
     * @date 2019/3/9
     * @param
     * @return org.springframework.security.crypto.password.PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //使用BCrypt加密
        return new BCryptPasswordEncoder();
    }

    /**
     * 装配authenticationProvider
     *
     * @author hmh
     * @date 2019/3/9
     * @param
     * @return org.springframework.security.authentication.AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        //数据库中密码以密文形式存储，设置加密方式
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    /**
     * 配置http请求相关资源权限
     *
     * @author hmh
     * @date 2019/3/9
     * @param http
     * @return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * 通过authorizeRequests()方法来开始请求权限配置。可以在authorizeRequests() 后定义多个antMatchers()配置器来控制
         * 不同的url接受不同权限的用户访问，而其中permitAll() 方法是运行所有权限用户包含匿名用户访问,也就是用户任意权限访问。
         */
        http.authorizeRequests()
                //配置static下的静态资源和首页可以任意访问
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/images/**", "*.ico", "/index").permitAll()
                //仅授权“ROLE_ADMIN”权限的访问“/admins/”下资源
                .antMatchers("/admins/**").hasRole("ADMIN")
                //配置h2控制台可以任意访问（便于测试）
                .antMatchers("/h2-console/**").permitAll()
                //需要权限的资源均需要登录验证（如匿名用户去访问权限页面），通过formLogin()授权认证，并自定义登录页和登录失败跳转的地址
                .and()
                .formLogin()
                .loginPage("/login").failureUrl("/login-error")
                //启用RememberMe功能，记住用户登录状态，下次可直接登录，使用key简单加密token的方式。
                .and().rememberMe().key(KEY)
                //处理异常，拒绝访问（没有权限如普通用户去访问后台管理页面）就重定向到 401 页面
                .and().exceptionHandling().accessDeniedPage("/401");
        //禁用 H2 控制台的CSRF防护（其余均需CSRF防护）
        http.csrf().ignoringAntMatchers("/h2-console/**");
        //允许来自同一来源的H2 控制台的请求
        http.headers().frameOptions().sameOrigin();
    }

    /**
     * 认证信息的管理（用来初始化AuthenticationManager），进行一些初始化的操作，最终会将认证信息存入安全上下文（SecurityContext）中
     *
     * @author hmh
     * @date 2019/3/9
     * @param auth
     * @return void
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

}
