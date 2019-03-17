package com.hmh.spring.boot.blog.domain;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

/**
 * Authority实现了GrantedAuthority接口，代表了一个用户权限实体类，该类存储了多种用户权限。
 * GrantedAuthority 对象代表赋予给当前用户的一种权限。GrantedAuthority 是一个接口，其通常是通过 UserDetailsService
 * 进行加载，然后赋予给 UserDetails 的。GrantedAuthority 中只定义了一个 getAuthority() 方法，该方法返回一个字符串，
 * 表示对应权限的字符串表示，如果对应权限不能用字符串表示，则应当返回 null。
 *
 * @author hmh
 * @date 2019/3/7
 */
@Entity
public class Authority implements GrantedAuthority {

//    private static final long serialVersionUID = 1L;

    /**
     * 主键（自增长策略）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限种类（映射字段字符串类型，不能为空）
     */
    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 需要满足接口规则getAuthority()命名，作用相当于getName()
     *
     * @author hmh
     * @date 2019/3/7
     * @param
     * @return java.lang.String
     */
    @Override
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
