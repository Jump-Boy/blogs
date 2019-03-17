package com.hmh.spring.boot.blog.repository;

import com.hmh.spring.boot.blog.domain.Catalog;
import com.hmh.spring.boot.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Catalog Dao层接口
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    /**
     * 根据用户查询博客分类
     *
     * @author hmh
     * @date 2019/3/12
     * @param user
     * @return java.util.List<com.hmh.spring.boot.blog.domain.Catalog>
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户及类目名称查询用户下的某个博客分类
     *
     * @author hmh
     * @date 2019/3/12
     * @param user
     * @param name
     * @return java.util.List<com.hmh.spring.boot.blog.domain.Catalog>
     */
    List<Catalog> findByUserAndName(User user,String name);

}
