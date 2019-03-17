package com.hmh.spring.boot.blog.service;

import com.hmh.spring.boot.blog.domain.Comment;

/**
 * Comment Service接口
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface CommentService {

    /**
     * 根据id获取 Comment
     *
     * @author hmh
     * @date 2019/3/12
     * @param id 博客id
     * @return com.hmh.spring.boot.blog.domain.Comment
     */
    Comment getCommentById(Long id);

    /**
     * 删除评论
     *
     * @author hmh
     * @date 2019/3/12
     * @param id 博客id
     * @return void
     */
    void removeComment(Long id);

}
