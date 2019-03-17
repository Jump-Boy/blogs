package com.hmh.spring.boot.blog.repository;

import com.hmh.spring.boot.blog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vote Dao层接口
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
