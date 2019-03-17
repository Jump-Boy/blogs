package com.hmh.spring.boot.blog.service.impl;

import com.hmh.spring.boot.blog.domain.Vote;
import com.hmh.spring.boot.blog.repository.VoteRepository;
import com.hmh.spring.boot.blog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Vote Service实现类
 *
 * @author hmh
 * @date 2019/3/12
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }

}
