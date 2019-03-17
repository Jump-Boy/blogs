package com.hmh.spring.boot.blog.service;

import com.hmh.spring.boot.blog.domain.Vote;

/**
 * Vote Service接口
 *
 * @author hmh
 * @date 2019/3/12
 */
public interface VoteService {

    /**
     * 根据id获取 Vote
     *
     * @author hmh
     * @date 2019/3/12
     * @param id
     * @return com.hmh.spring.boot.blog.domain.Vote
     */
    Vote getVoteById(Long id);

    /**
     * 删除某个Vote
     *
     * @author hmh
     * @date 2019/3/12
     * @param id
     * @return void
     */
    void removeVote(Long id);

}
