package com.hmh.spring.boot.blog.service.impl;

import com.hmh.spring.boot.blog.domain.*;
import com.hmh.spring.boot.blog.repository.BlogRepository;
import com.hmh.spring.boot.blog.service.BlogService;
import com.hmh.spring.boot.blog.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

/**
 * Blog服务实现类
 *
 * @author hmh
 * @date 2019/3/11
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    /**
     * 保存博客（同步更新ES）
     *
     * @author hmh
     * @date 2019/3/12
     * @param blog
     * @return com.hmh.spring.boot.blog.domain.Blog
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //是否新建博客
        boolean isNew = (blog.getId() == null);
        EsBlog esBlog = null;
        //不管新建还是更改，插入数据库中直接save
        Blog returnBlog = blogRepository.save(blog);
        //ES同步更新
        if (isNew) {
            //如果是新建博客
            esBlog = new EsBlog(returnBlog);
        } else {
            //若不是新建博客，则先从ES中查出该博客，然后再保存。因为存在的文档是有BlogId的，不能重复插入
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }
        //最终还是调用es save，通过id主键去保存
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    /**
     * 删除博客（同步ES）
     *
     * @author hmh
     * @date 2019/3/12
     * @param id
     * @return void
     */
    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
        //先通过blogid查询es文档
        EsBlog esblog = esBlogService.getEsBlogByBlogId(id);
        //然后通过主键删除
        esBlogService.removeEsBlog(esblog.getId());
    }

    @Transactional
    @Override
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        String tags = title;
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user, tags, user, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize()+1);
        //不要调用 blogRepository.save(blog)，而应该调用this.saveBlog(blog) ，需要同步es
        this.saveBlog(blog);
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        //查询出博客信息，最终将评论信息添加到博客中
        Blog originalBlog = blogRepository.findOne(blogId);
        //从上下文中获取用户信息，关联comment（comment有user_id外键）
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user, commentContent);
        originalBlog.addComment(comment);
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeComment(commentId);
        this.saveBlog(originalBlog);
    }

    @Override
    public Blog createVote(Long blogId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        //当前系统登录已认证的用户进行点赞
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist = originalBlog.addVote(vote);
        //如果点赞失败，说明已点赞过
        if (isExist) {
            throw new IllegalArgumentException("该用户已经点过赞了");
        }
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeVote(voteId);
        this.saveBlog(originalBlog);
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog, pageable);
        return blogs;
    }

}
