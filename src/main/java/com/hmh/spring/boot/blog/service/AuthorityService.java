package com.hmh.spring.boot.blog.service;

import com.hmh.spring.boot.blog.domain.Authority;

/**
 * 权限 Service接口
 *
 * @author hmh
 * @date 2019/3/7
 */
public interface AuthorityService {

    /**
     * 根据id获取Authority（权限信息）
     *
     * @author hmh
     * @date 2019/3/7
     * @param id
     * @return com.hmh.spring.boot.blog.domain.Authority
     */
    Authority getAuthorityById(Long id);

}
