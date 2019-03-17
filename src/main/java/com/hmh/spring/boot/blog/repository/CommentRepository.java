package com.hmh.spring.boot.blog.repository;

import com.hmh.spring.boot.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment Repository接口
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
