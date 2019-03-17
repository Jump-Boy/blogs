package com.hmh.spring.boot.blog.service;

import com.hmh.spring.boot.blog.domain.Blog;
import com.hmh.spring.boot.blog.domain.Catalog;
import com.hmh.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 博客Service接口
 *
 * @author hmh
 * @date 2019/3/11
 */
public interface BlogService {

    /**
     * 保存Blog
     *
     * @author hmh
     * @date 2019/3/11
     * @param blog
     * @return com.hmh.spring.boot.blog.domain.Blog
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除Blog
     *
     * @author hmh
     * @date 2019/3/11
     * @param id
     * @return void
     */
    void removeBlog(Long id);

    /**
     * 更新Blog
     *
     * @author hmh
     * @date 2019/3/11
     * @param blog
     * @return com.hmh.spring.boot.blog.domain.Blog
     */
    Blog updateBlog(Blog blog);

    /**
     * 根据id获取Blog
     *
     * @author hmh
     * @date 2019/3/11
     * @param id
     * @return com.hmh.spring.boot.blog.domain.Blog
     */
    Blog getBlogById(Long id);

    /**
     * 根据标题进行分页模糊查询（最新）
     *
     * @author hmh
     * @date 2019/3/11
     * @param user
     * @param title
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.Blog>
     */
    Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable);

    /**
     * 根据用户名进行分页模糊查询（最热）
     *
     * @author hmh
     * @date 2019/3/11
     * @param user
     * @param title
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.Blog>
     */
    Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable);

    /**
     * 阅读量递增
     *
     * @author hmh
     * @date 2019/3/11
     * @param id
     * @return void
     */
    void readingIncrease(Long id);

    /**
     * 发表评论
     *
     * @author hmh
     * @date 2019/3/12
     * @param blogId
     * @param commentContent
     * @return com.hmh.spring.boot.blog.domain.Blog
     */
    Blog createComment(Long blogId, String commentContent);

    /**
     * 删除评论
     *
     * @author hmh
     * @date 2019/3/12
     * @param blogId
     * @param commentId
     * @return void
     */
    void removeComment(Long blogId, Long commentId);

    /**
     * 点赞
     *
     * @author hmh
     * @date 2019/3/12
     * @param blogId
     * @return com.hmh.spring.boot.blog.domain.Blog
     */
    Blog createVote(Long blogId);

    /**
     * 取消点赞
     *
     * @author hmh
     * @date 2019/3/12
     * @param blogId
     * @param voteId
     * @return void
     */
    void removeVote(Long blogId, Long voteId);

    /**
     * 查询某个分类下的博客
     *
     * @author hmh
     * @date 2019/3/12
     * @param catalog
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.Blog>
     */
    Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);

}
