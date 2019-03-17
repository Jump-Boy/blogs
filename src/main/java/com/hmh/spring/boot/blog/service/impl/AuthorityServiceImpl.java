package com.hmh.spring.boot.blog.service.impl;

import com.hmh.spring.boot.blog.domain.Authority;
import com.hmh.spring.boot.blog.repository.AuthorityRepository;
import com.hmh.spring.boot.blog.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限Authority Service实现
 *
 * @author hmh
 * @date 2019/3/7
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * 根据id获取Authority（权限信息）
     *
     * @param id
     * @return com.hmh.spring.boot.blog.domain.Authority
     * @author hmh
     * @date 2019/3/7
     */
    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}
