package com.hmh.spring.boot.blog.service.impl;

import com.hmh.spring.boot.blog.domain.User;
import com.hmh.spring.boot.blog.repository.UserRepository;
import com.hmh.spring.boot.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 用户Services实现类（需要额外实现UserDetailsService，我们的User类相当于UserDetails，所以UserService还需要完成获取相关认证信息的功能）
 *
 * @author hmh
 * @date 2019/3/6
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 保存用户
     *
     * @param user 用户信息
     * @return com.hmh.spring.boot.blog.domain.User
     * @author hmh
     * @date 2019/3/6
     */
    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return com.hmh.spring.boot.blog.domain.User
     * @author hmh
     * @date 2019/3/6
     */
    @Transactional
    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return void
     * @author hmh
     * @date 2019/3/6
     */
    @Transactional
    @Override
    public void removeUser(Long id) {
        userRepository.delete(id);
    }

    /**
     * 删除列表里面的所有用户
     *
     * @param users 需要删除的用户列表
     * @return void
     * @author hmh
     * @date 2019/3/6
     */
    @Override
    public void removeUsersInBatch(List<User> users) {

    }

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return com.hmh.spring.boot.blog.domain.User
     * @author hmh
     * @date 2019/3/6
     */
    @Override
    public User updateUser(User user) {
        return null;
    }

    /**
     * 根据id获取用户
     *
     * @param id 用户id
     * @return com.hmh.spring.boot.blog.domain.User
     * @author hmh
     * @date 2019/3/6
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    /**
     * 获取所有用户列表
     *
     * @return java.util.List<com.hmh.spring.boot.blog.domain.User>
     * @author hmh
     * @date 2019/3/6
     */
    @Override
    public List<User> listUsers() {
        return null;
    }

    /**
     * 根据用户名进行分页模糊查询
     *
     * @param name     用户姓名
     * @param pageable 分页参数
     * @return Page<User>
     * @author hmh
     * @date 2019/3/6
     */
    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        //进行模糊查询，需要对参数进行处理，要查询出包含该参数的，所以前后加上“%”，然后JPA会like
        name = "%" + name + "%";
        Page<User> users = userRepository.findByNameLike(name, pageable);
        return users;
    }

    /**
     * 实现认证信息的获取
     * 登录认证的时候Spring Security会通过UserDetailsService的loadUserByUsername()方法获取对应的UserDetails进行认证
     *
     * @author hmh
     * @date 2019/3/9
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> listUsersByUsernames(Collection<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
    }

}
