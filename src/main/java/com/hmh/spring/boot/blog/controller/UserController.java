package com.hmh.spring.boot.blog.controller;

import com.hmh.spring.boot.blog.domain.Authority;
import com.hmh.spring.boot.blog.domain.User;
import com.hmh.spring.boot.blog.repository.UserRepository;
import com.hmh.spring.boot.blog.service.AuthorityService;
import com.hmh.spring.boot.blog.service.UserService;
import com.hmh.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.hmh.spring.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

/**
 * user controller
 *
 * @author hmh
 * @date 2019/3/2
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    /**
     * 后台首页"用户管理"接口，分页查询所有用户或者指定用户名模糊查询 url: /users
     *
     * @author hmh
     * @date 2019/3/6
     * @param async 用于区分首次页面加载还是之后分页查询的一个标识(非必传)
     * @param pageIndex 需要查询的页索引(非必传,默认0)
     * @param pageSize 查询的页面大小,也就是需要查询的记录数(非必传,默认10)
     * @param name 需要查询的姓名(非必传,默认空)
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping
    public ModelAndView list(@RequestParam(value = "async", required = false) boolean async,
                             @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                             @RequestParam(value = "name", required = false, defaultValue = "") String name,
                             Model model) {
        //分页查询参数对象
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        //page为查询出的分页数据对象(包含了数据列表以及一些分页参数如总数据数等)
        Page<User> page = userService.listUsersByNameLike(name, pageable);
        //从page中获取查询出的数据列表
        List<User> list = page.getContent();
        //"users/list"页面填充list数据
        model.addAttribute("userList", list);
        //"users/list"中引入的"fragments/page"填充page的分页信息(首次加载admins页面,填充的分页信息为默认值0和10)
        model.addAttribute("page", page);
        /*
         * 如果是点击"用户管理"或者初次加载admins页面的话,async默认为false,那么admins/main.js将填充整个users/list至admins页面右侧#rightContainer div,
         * 否则已经加载了用户管理,然后点击下方分页查看,users/list会调用users/main.js发起分页查询,async为true,此时应该是仅仅去刷新#mainContainer
         * 这部分中间的数据,所以只返回mainContainerRepleace这一部分片段即可,而如果我们users/main.js去重新渲染#rightContainer的话,返回users/list
         * 这个div,那么下方的分页信息page也会被刷新重置,显然不对.
         */
        return new ModelAndView(async == true ? "users/list :: #mainContainerRepleace" : "users/list", "userModel", model);
    }

    /**
     * 根据id查询用户信息
     *
     * @author hmh
     * @date 2019/3/2
     * @param id
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("title", "查看用户");
        return new ModelAndView("users/view", "userModel", model);
    }

    /**
     * 新增用户接口，获取form表单页面（初始化form表单页面）
     * "users/list"页面搜索框旁的“+”按钮，绑定了点击事件在“users/main.js”中，通过调用该接口返回"users/add"(一个form表单页)填充至“users/list”
     * 中的userFormContainer div，点击“提交”触发“users/main.js”中的事件，获取form表单数据并访问“/users”post，js重新访问刷新list的数据域
     *
     * @author hmh
     * @date 2019/3/2
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        model.addAttribute("user", new User(null, null, null, null));
        return new ModelAndView("users/add", "userModel", model);
    }

    /**
     * 新增用户或更新用户接口，最终返回指定响应信息即可，若新增或更新成功后前天会刷新admins页面的右侧部分
     *
     * @author hmh
     * @date 2019/3/7
     * @param user 用户信息
     * @param authorityId 用户分配的权限id
     * @return org.springframework.http.ResponseEntity<com.hmh.spring.boot.blog.vo.Response>
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(User user, Long authorityId) {
        List<Authority> authorities = new LinkedList<>();
        authorities.add(authorityService.getAuthorityById(authorityId));
        //设置用户的权限属性(对角色进行授权)（jpa会插入用户权限中间表中）
        user.setAuthorities(authorities);

        //如果用户不存在，为新增操作，那么直接加密密码即可。否则，如果存在，即更新操作，先比较密码是否有变更，有则加密新密码
        if(user.getId() == null) {
            // 加密密码
            user.setEncodePassword(user.getPassword());
        }else {
            // 判断密码是否做了变更
            User originalUser = userService.getUserById(user.getId());
            String rawPassword = originalUser.getPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePasswd = encoder.encode(user.getPassword());
            boolean isMatch = encoder.matches(rawPassword, encodePasswd);
            if (!isMatch) {
                //密码更改了，加密密码保存
                user.setEncodePassword(user.getPassword());
            }else {
                //未更改，设置原密码
                user.setPassword(user.getPassword());
            }
        }

        try {
            user = userService.saveUser(user);
        } catch (TransactionSystemException e) {
            /*
             *  我们字段添加约束检查，所以在不满足约束的情况下，抛出异常的，所以需要对ConstraintViolationException（约束异常）进行捕获，然后格式化返回页面。
             *  但是因为在service层加了事务控制，所以发生约束异常时，ConstraintViolationException 会被 RollbackException 包裹，RollbackException 又被
             *  TransactionSystemException 包裹，所以需要捕获事务异常，然后一层层向下去找，直到找到约束异常，然后格式化返回。
             */
            Throwable t = e.getCause();
            //t == null 说明找到了最后一层异常了
            while(t != null) {
                if (t instanceof ConstraintViolationException) {
                    return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage((ConstraintViolationException) t)));
                } else {
                    //继续向下一层找
                    t = t.getCause();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

    /**
     * admins删除用户接口，返回指定格式响应，若删除成功前台页面会自动刷新
     *
     * @author hmh
     * @date 2019/3/2
     * @param id 用户id
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
        try {
            userService.removeUser(id);
        } catch (Exception e) {
            return  ResponseEntity.ok().body( new Response(false, e.getMessage()));
        }
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }

    /**
     * 修改编辑用户回显接口（根据该id查询user，将用户信息回显到form表单上，然后再点击修改提交，调/users post完成更新）
     *
     * @author hmh
     * @date 2019/3/2
     * @param id 编辑用户的id
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("users/edit", "userModel", model);
    }

}
