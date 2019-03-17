package com.hmh.spring.boot.blog.service;

import com.hmh.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author hmh
 * @date 2019/3/6
 */
public interface UserService {

    /**
     * 保存用户
     *
     * @author hmh
     * @date 2019/3/6
     * @param user 用户信息
     * @return com.hmh.spring.boot.blog.domain.User
     */
    User saveUser(User user);

    /**
     * 用户注册
     *
     * @author hmh
     * @date 2019/3/6
     * @param user 用户信息
     * @return com.hmh.spring.boot.blog.domain.User
     */
    User registerUser(User user);

    /**
     * 删除用户
     *
     * @author hmh
     * @date 2019/3/6
     * @param id 用户id
     * @return void
     */
    void removeUser(Long id);

    /**
     * 删除列表里面的所有用户
     *
     * @author hmh
     * @date 2019/3/6
     * @param users 需要删除的用户列表
     * @return void
     */
    void removeUsersInBatch(List<User> users);

    /**
     * 更新用户
     *
     * @author hmh
     * @date 2019/3/6
     * @param user 用户信息
     * @return com.hmh.spring.boot.blog.domain.User
     */
    User updateUser(User user);

    /**
     * 根据id获取用户
     *
     * @author hmh
     * @date 2019/3/6
     * @param id 用户id
     * @return com.hmh.spring.boot.blog.domain.User
     */
    User getUserById(Long id);

    /**
     * 获取所有用户列表
     *
     * @author hmh
     * @date 2019/3/6
     * @param
     * @return java.util.List<com.hmh.spring.boot.blog.domain.User>
     */
    List<User> listUsers();

    /**
     * 根据用户名进行分页模糊查询
     *
     * @author hmh
     * @date 2019/3/6
     * @param name 用户姓名
     * @param pageable 分页参数
     * @return Page<User>
     */
    Page<User> listUsersByNameLike(String name, Pageable pageable);

    /**
     * 根据名称列表查询
     *
     * @author hmh
     * @date 2019/3/12
     * @param usernames
     * @return java.util.List<com.hmh.spring.boot.blog.domain.User>
     */
    List<User> listUsersByUsernames(Collection<String> usernames);

}
