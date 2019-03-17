package com.hmh.spring.boot.blog.controller;

import com.hmh.spring.boot.blog.domain.Authority;
import com.hmh.spring.boot.blog.domain.User;
import com.hmh.spring.boot.blog.service.AuthorityService;
import com.hmh.spring.boot.blog.service.UserService;
import com.hmh.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

/**
 * 主页控制器
 *
 * @author hmh
 * @date 2019/3/5
 */
@Controller
public class MainController {

    /**
     * 博主（普通用户）权限类型，因为只有普通用户有注册，管理员无注册，所以定义常量（1：管理员；2：博主）
     */
    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    /**
     * 项目根路径，如果访问根路径“/”，那么重定向到首页“/index”。
     *
     * @author hmh
     * @date 2019/3/5
     * @param
     * @return java.lang.String
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    /**
     * 首页
     *
     * @author hmh
     * @date 2019/3/5
     * @param
     * @return java.lang.String
     */
    @GetMapping("/index")
    public String index() {
        return "redirect:/blogs";
    }

    /**
     * 登录
     *
     * @author hmh
     * @date 2019/3/5
     * @param
     * @return java.lang.String
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 若登录失败，则重定向访问“/login-error”，然后将错误信息填充，并返回登录页
     *
     * @author hmh
     * @date 2019/3/5
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登录失败，用户名或者密码错误！");
        return "login";
    }

    /**
     * "注册"按钮接口，返回注册form页面
     *
     * @author hmh
     * @date 2019/3/5
     * @param
     * @return java.lang.String
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 注册form提交按钮接口，完成注册功能
     *
     * @author hmh
     * @date 2019/3/7
     * @param user 用户信息
     * @return java.lang.String
     */
    @PostMapping("/register")
    public ModelAndView registerUser(User user, Model model) {
        List<Authority> authorities = new LinkedList<>();
        //普通用户才有注册（对普通用户进行授权）
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);

        //发生错误，返回错误页面
        String errorMessage;
        try {
            userService.saveUser(user);
        }catch (TransactionSystemException e) {
            Throwable t = e.getCause();
            //t == null 说明找到了最后一层异常了
            while(t != null) {
                if (t instanceof ConstraintViolationException) {
                    errorMessage =  ConstraintViolationExceptionHandler.getMessage((ConstraintViolationException) t);
                    model.addAttribute("errorMessage", errorMessage);
                    return new ModelAndView("registerError", "model", model);
                } else {
                    //继续向下一层找
                    t = t.getCause();
                }
            }
        } catch (Exception e) {
            errorMessage =  e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return new ModelAndView("registerError", "model", model);
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/401")
    public String accessDenied(){
        return "401";
    }

}
