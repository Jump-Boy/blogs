package com.hmh.spring.boot.blog.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体类（UserDetails是Spring Security中一个核心的接口。其中定义了一些可以获取用户名、密码、权限等与认证相关的信息的方法。
 * Spring Security内部使用的UserDetails实现类大都是内置的User类，我们如果要使用UserDetails时也可以直接使用该类。但是，
 * 因为这里我们user还包含了email等其他信息，只包含有认证相关信息的UserDetails对象可能就不能满足我们的要求了，所以我们需要
 * 实现UserDetail来自定义我们的User类）
 *
 * @author hmh
 * @date 2019/3/1
 */
@Entity
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id，实体的唯一标识（设置主键并自增策略）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户姓名（添加Bean校验，不能为空且字符长度在2-20之间，同时添加JPA映射，设置sql字段属性）
     */
    @NotEmpty(message = "姓名不能为空")
    @Size(min = 2, max = 20)
    @Column(nullable = false, length = 20) // 映射为字段，值不能为空
    private String name;

    /**
     * 用户邮箱（添加Bean校验，不能为空，长度不超过50且添加邮箱格式校验，同时JPA映射设置字段唯一约束）
     */
    @NotEmpty(message = "邮箱不能为空")
    @Size(max = 50)
    @Email(message= "邮箱格式不对" )
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    /**
     * 用户账号，用户登录时的唯一标识
     */
    @NotEmpty(message = "账号不能为空")
    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20, unique = true)
    private String username;

    /**
     * 登录时密码
     */
    @NotEmpty(message = "密码不能为空")
    @Size(max = 100)
    @Column(length = 100)
    private String password;

    /**
     * 头像图片地址
     */
    @Column(length = 200)
    private String avatar;

    /**
     * 用户的权限列表，多对多的一种关系（User和Authority）
     * 建立中间表user_authority，user_id和user.id对应；authority_id和authority.id对应
     * 一个用户可能有多种角色权限类型，一种权限可能多个用户都有，所以多对多
     */
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    /**
     * JPA 的规范要求无参构造函数：设为 protected 防止直接使用
     */
    protected User() {}

    public User(Long id, String name, String username, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * thymeleaf在${user.authorities}调用该方法，想要在页面上显示的是权限的字串名字，而不是显示Authorities对象的id和字串。
     * Spring Security针对GrantedAuthority有一个简单实现SimpleGrantedAuthority。该类只是简单的接收一个表示权限的字符串。
     * 所以需要对List<Authority>进行处理，将Authority转成SimpleGrantedAuthority
     *
     * @author hmh
     * @date 2019/3/7
     * @param
     * @return java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //  需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
        List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
        for(GrantedAuthority authority : this.authorities){
            simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return simpleAuthorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * BCrypt加密密码
     *
     * @author hmh
     * @date 2019/3/7
     * @param password
     * @return void
     */
    public void setEncodePassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(password);
        this.password = encodePasswd;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, username='%s', name='%s', email='%s', password='%s']", id, username, name, email,
                password);
    }

}
