package com.hmh.spring.boot.blog.repository;

import com.hmh.spring.boot.blog.domain.Blog;
import com.hmh.spring.boot.blog.domain.Catalog;
import com.hmh.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 博客Dao层接口
 *
 * @author hmh
 * @date 2019/3/11
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * 根据用户名以及标题模糊分页查询博客列表（按创建时间逆序排列）
     *
     * @author hmh
     * @date 2019/3/11
     * @param user
     * @param title
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.Blog>
     */
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    /**
     * 根据用户名以及标题模糊分页查询博客列表
     *
     * @author hmh
     * @date 2019/3/11
     * @param user
     * @param title
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.Blog>
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    /**
     *
     *
     * @author hmh
     * @date 2019/3/12
     * @param title
     * @param user
     * @param tags
     * @param user2
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.Blog>
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user, String tags, User user2, Pageable pageable);

    /**
     * 根据分类查询博客列表
     *
     * @author hmh
     * @date 2019/3/12
     * @param catalog
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.Blog>
     */
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);

}
