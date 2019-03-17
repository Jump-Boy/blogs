package com.hmh.spring.boot.blog.controller;

import com.hmh.spring.boot.blog.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;

/**
 * 后台管理控制器
 *
 * @author hmh
 * @date 2019/3/5
 */
@Controller
@RequestMapping("/admins")
public class AdminController {

    /**
     * 后台管理首页展示：管理员后台管理所有用户
     *
     * @author hmh
     * @date 2019/3/5
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping
    public ModelAndView listUsers(Model model) {
        //后台菜单类目集合（包含了所有左侧菜单类目）
        List<Menu> list = new LinkedList<>();
        list.add(new Menu("用户管理", "/users"));
        list.add(new Menu("角色管理", "/roles"));
        list.add(new Menu("博客管理", "/blogs"));
        list.add(new Menu("评论管理", "/commits"));
        model.addAttribute("list", list);
        return new ModelAndView("admins/index", "model", model);
    }

}
