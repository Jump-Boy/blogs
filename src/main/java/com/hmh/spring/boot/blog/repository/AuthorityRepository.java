package com.hmh.spring.boot.blog.repository;

import com.hmh.spring.boot.blog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限Authority Dao接口
 *
 * @author hmh
 * @date 2019/3/7
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
