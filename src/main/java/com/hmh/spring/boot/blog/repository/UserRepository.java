package com.hmh.spring.boot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hmh.spring.boot.blog.domain.User;

import java.util.Collection;
import java.util.List;

/**
 * 用户Dao层接口
 *
 * @author hmh
 * @date 2019/3/6
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户姓名分页模糊查询用户列表
     *
     * @author hmh
     * @date 2019/3/6
     * @param name 用户姓名
     * @param pageable 分页参数
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.User>
     */
    Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 根据用户账号查询某个用户
     *
     * @author hmh
     * @date 2019/3/6
     * @param username 用户账号
     * @return com.hmh.spring.boot.blog.domain.User
     */
    User findByUsername(String username);

    /**
     * 根据名称列表查询
     *
     * @author hmh
     * @date 2019/3/12
     * @param usernames 用户名列表集合
     * @return java.util.List<com.hmh.spring.boot.blog.domain.User>
     */
    List<User> findByUsernameIn(Collection<String> usernames);

}
