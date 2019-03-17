package com.hmh.spring.boot.blog.service;

import com.hmh.spring.boot.blog.domain.Catalog;
import com.hmh.spring.boot.blog.domain.User;

import java.util.List;

/**
 * Catalog Service接口
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface CatalogService {

    /**
     * 保存Catalog
     *
     * @author hmh
     * @date 2019/3/12
     * @param catalog
     * @return com.hmh.spring.boot.blog.domain.Catalog
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     *
     * @author hmh
     * @date 2019/3/12
     * @param id
     * @return void
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取Catalog
     *
     * @author hmh
     * @date 2019/3/12
     * @param id
     * @return com.hmh.spring.boot.blog.domain.Catalog
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取某个用户下的Catalog列表
     *
     * @author hmh
     * @date 2019/3/12
     * @param user
     * @return java.util.List<com.hmh.spring.boot.blog.domain.Catalog>
     */
    List<Catalog> listCatalogs(User user);

}
