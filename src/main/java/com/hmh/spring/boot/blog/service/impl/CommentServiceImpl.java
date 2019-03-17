package com.hmh.spring.boot.blog.service.impl;

import com.hmh.spring.boot.blog.domain.Comment;
import com.hmh.spring.boot.blog.repository.CommentRepository;
import com.hmh.spring.boot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * CommentService接口实现
 *
 * @author hmh
 * @date 2019/3/12
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

}
