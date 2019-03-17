package com.hmh.spring.boot.blog.controller;

import com.hmh.spring.boot.blog.domain.User;
import com.hmh.spring.boot.blog.service.BlogService;
import com.hmh.spring.boot.blog.service.VoteService;
import com.hmh.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.hmh.spring.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;

/**
 * @author hmh
 * @date 2019/3/12
 */
@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    /**
     * 发表点赞（指定角色权限管理员和用户才能操作方法，必须登录认证）
     *
     * @author hmh
     * @date 2019/3/12
     * @param blogId
     * @return org.springframework.http.ResponseEntity<com.hmh.spring.boot.blog.vo.Response>
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createVote(Long blogId) {
        try {
            //如果已经点过赞或者点赞失败，会抛异常
            blogService.createVote(blogId);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }

    /**
     * 删除（取消）点赞（指定角色权限才能操作方法）
     * 取消点赞就是从点赞表中移除某条点赞记录，当前这篇博客若是登录用户点赞过的，那么页面点赞按钮隐藏，取消点赞按钮显示且包含了用户那条点赞记录的
     * 信息，当点取消时，会将vote的id传到该接口
     *
     * @author hmh
     * @date 2019/3/12
     * @param id
     * @param blogId
     * @return org.springframework.http.ResponseEntity<com.hmh.spring.boot.blog.vo.Response>
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, Long blogId) {
        boolean isOwner = false;
        //根据vote id查询出操作的用户信息
        User user = voteService.getVoteById(id).getUser();

        // 需要检验，判断操作用户是否是点赞的所有者
        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }
        if (!isOwner) {
            return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
        }
        //是当前用户点赞，可以进行取消点赞操作
        try {
            blogService.removeVote(blogId, id);
            voteService.removeVote(id);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));
    }
}
