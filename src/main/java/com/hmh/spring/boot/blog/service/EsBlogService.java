package com.hmh.spring.boot.blog.service;

import com.hmh.spring.boot.blog.domain.EsBlog;
import com.hmh.spring.boot.blog.domain.User;
import com.hmh.spring.boot.blog.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * EsBlog Service接口
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface EsBlogService {

    /**
     * 删除Blog
     *
     * @author hmh
     * @date 2019/3/12
     * @param id
     * @return void
     */
    void removeEsBlog(String id);

    /**
     * 更新 EsBlog
     *
     * @author hmh
     * @date 2019/3/12
     * @param esBlog
     * @return com.hmh.spring.boot.blog.domain.EsBlog
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 根据id获取Blog
     *
     * @author hmh
     * @date 2019/3/12
     * @param blogId
     * @return com.hmh.spring.boot.blog.domain.EsBlog
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 最新博客列表，分页
     *
     * @author hmh
     * @date 2019/3/12
     * @param keyword
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.EsBlog>
     */
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    /**
     * 最热博客列表，分页
     *
     * @author hmh
     * @date 2019/3/12
     * @param keyword
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.EsBlog>
     */
    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable);

    /**
     * 博客列表，分页
     *
     * @author hmh
     * @date 2019/3/12
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.EsBlog>
     */
    Page<EsBlog> listEsBlogs(Pageable pageable);

    /**
     * 最新前5
     *
     * @author hmh
     * @date 2019/3/12
     * @param
     * @return List<EsBlog>
     */
    List<EsBlog> listTop5NewestEsBlogs();

    /**
     * 最热前5
     *
     * @author hmh
     * @date 2019/3/12
     * @param
     * @return java.util.List<com.hmh.spring.boot.blog.domain.EsBlog>
     */
    List<EsBlog> listTop5HotestEsBlogs();

    /**
     * 最热前 30 标签
     *
     * @author hmh
     * @date 2019/3/12
     * @param
     * @return java.util.List<com.hmh.spring.boot.blog.vo.TagVO>
     */
    List<TagVO> listTop30Tags();

    /**
     * 最热前12用户
     *
     * @author hmh
     * @date 2019/3/12
     * @param
     * @return java.util.List<com.hmh.spring.boot.blog.domain.User>
     */
    List<User> listTop12Users();

}
