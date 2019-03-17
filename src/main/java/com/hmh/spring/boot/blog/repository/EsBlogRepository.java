package com.hmh.spring.boot.blog.repository;

import com.hmh.spring.boot.blog.domain.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Blog ES Dao层接口.
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

    /**
     * 模糊查询(去重)
     *
     * @author hmh
     * @date 2019/3/12
     * @param title
     * @param Summary
     * @param content
     * @param tags
     * @param pageable
     * @return org.springframework.data.domain.Page<com.hmh.spring.boot.blog.domain.EsBlog>
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title, String Summary, String content, String tags, Pageable pageable);

    EsBlog findByBlogId(Long blogId);
}
