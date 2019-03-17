package com.hmh.spring.boot.blog.controller;

import com.hmh.spring.boot.blog.domain.Blog;
import com.hmh.spring.boot.blog.domain.Catalog;
import com.hmh.spring.boot.blog.domain.User;
import com.hmh.spring.boot.blog.domain.Vote;
import com.hmh.spring.boot.blog.service.BlogService;
import com.hmh.spring.boot.blog.service.CatalogService;
import com.hmh.spring.boot.blog.service.UserService;
import com.hmh.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.hmh.spring.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 用户主页空间控制器
 *
 * @author hmh
 * @date 2019/3/5
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    /**
     * 用户首页，展示出指定用户名的用户页
     *
     * @author hmh
     * @date 2019/3/11
     * @param username 用户名（必传）
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
//        User  user = (User)userDetailsService.loadUserByUsername(username);
//        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * 获取个人设置页面（点击“header”中的个人设置，请求该接口，返回视图并渲染）
     * authentication.name.equals(#username) 用来限制仅当前授权用户可访问，即A用户不可访问其他用户的个人设置页面
     *
     * @author hmh
     * @date 2019/3/11
     * @param username
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        //文件服务器地址需要返回前端页面（在/userspace/main.js中的头像上传中已经写明了文件服务器地址）
//        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/profile", "userModel", model);
    }

    /**
     * 保存个人设置（/userspace/profile的form表单接口）
     * authentication.name.equals(#username) 用来限制仅当前授权用户可访问
     *
     * @author hmh
     * @date 2019/3/11
     * @param username
     * @param user
     * @return java.lang.String
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username,User user) {
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());

        // 判断密码是否做了变更
        String rawPassword = originalUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }

        userService.saveUser(originalUser);
        return "redirect:/u/" + username + "/profile";
    }

    /**
     * 获取编辑头像的界面
     *
     * @author hmh
     * @date 2019/3/11
     * @param username
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User  user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", model);
    }

    /**
     * 保存头像
     *
     * @author hmh
     * @date 2019/3/11
     * @param username
     * @param user
     * @return org.springframework.http.ResponseEntity<com.hmh.spring.boot.blog.vo.Response>
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
        //前端页面上传到文件服务器后返回的图片url
        String avatarUrl = user.getAvatar();
        //补全用户信息，主要补全头像信息
        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveUser(originalUser);

        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }

    /**
     * 某个用户的博客信息展示，默认搜索当前用户名下的所有博客，按最新的顺序排列
     *
     * @author hmh
     * @date 2019/3/5
     * @param username 用户名（必传）
     * @param order 博客排序规则（非必传，默认new，按最新排列）
     * @param catalogId 博客分类（非必传）
     * @param keyword 关键字（非必传，关键字可以是标签）
     * @param async 用于区分首次页面加载还是之后分页查询的一个标识(非必传)
     * @param pageIndex 开始查询的条数（第几条）
     * @param pageSize 需要查询的条数多少
     * @return java.lang.String
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "catalog", required = false) Long catalogId,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "" ) String keyword,
                                   @RequestParam(value = "async", required = false) boolean async,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   Model model) {
        User  user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);

        Page<Blog> page = null;
        //如果分类不为空
        if (catalogId != null) {
            Catalog catalog = catalogService.getCatalogById(catalogId);
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByCatalog(catalog, pageable);
            order = "";
        } else if (order.equals("hot")) {
            // 最热查询（多条件排序，是首先按照第一个字段排序，如果第一个字段相同的，再按照第二个字段来再次排序）
            Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleLikeAndSort(user, keyword, pageable);
        } else if (order.equals("new")) {
            // 最新查询
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleLike(user, keyword, pageable);
        }
        //当前所在页面数据列表
        List<Blog> list = page.getContent();

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return (async == true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u");
    }

    /**
     * 展示某个用户下的某篇博客
     *
     * @author hmh
     * @date 2019/3/5
     * @param username 该篇博客的用户名（哪一个用户的博客，必传）
     * @param id 博客id（必传）
     * @param model
     * @return java.lang.String
     *
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        User principal = null;
        Blog blog = blogService.getBlogById(id);

        // 每次读取，简单的可以认为阅读量增加1次（不管是别人预览还是自己预览，都算浏览量）
        blogService.readingIncrease(id);
        //是否是用户自己的博客
        boolean isBlogOwner = false;
        /*
         * 判断操作用户是否是博客的所有者，用于决定该博客是否可编辑，只有本人才可编辑（有用户信息且不是匿名用户。会从session中初始化SecurityContext，
         * 也就是可以理解为判断session中的用户信息是否和所传用户名一致，不一致则不是本人博客）
         */
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //参数的用户名是否与security context中的用户名一致
            if (principal !=null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
        }

        /*
         * 获取当前博客的点赞情况，遍历，判断当前用户是否有点过赞，点过，则将那条点赞记录传至前台。前台根据点赞记录判断，若有则显示“取消点赞”
         * 并隐藏点赞。否则显示“点赞”，隐藏“取消点赞”
         */
        List<Vote> votes = blog.getVotes();
        Vote currentVote = null;
        if (principal !=null) {
            for (Vote vote : votes) {
                if (vote.getUser().getUsername().equals(principal.getUsername())) {
                    currentVote = vote;
                }
                break;
            }
        }

        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel", blogService.getBlogById(id));
        model.addAttribute("currentVote",currentVote);
        return "userspace/blog";
    }

    /**
     * 删除博客
     *
     * @author hmh
     * @date 2019/3/11
     * @param username 需要删除的博客的用户名
     * @param id 删除的博客id
     * @return org.springframework.http.ResponseEntity<com.hmh.spring.boot.blog.vo.Response>
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id") Long id) {

        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    /**
     * 获取新增博客的界面
     *
     * @author hmh
     * @date 2019/3/12
     * @param username
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
        //查询出用户名对应下的博客分类，填充页面，在新增页面上可以显示现有的博客分类
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("catalogs", catalogs);
        return new ModelAndView("userspace/blogedit", "blogModel", model);
    }

    /**
     * 获取编辑博客的界面
     *
     * @author hmh
     * @date 2019/3/11
     * @param username 当前博主用户名（必传）
     * @param id 编辑的博客id（必传）
     * @param model
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("catalogs", catalogs);
        return new ModelAndView("userspace/blogedit", "blogModel", model);
    }

    /**
     * 保存博客（新增博客或者编辑更新博客均可）
     *
     * @author hmh
     * @date 2019/3/11
     * @param username 用户名
     * @param blog 博客信息（JSON格式，必传）
     * @return org.springframework.http.ResponseEntity<com.hmh.spring.boot.blog.vo.Response>
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
        //对 Catalog 进行空处理（要求必须先选择分类，才可保存成功）
        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        try {
            //若是新增的博客，则需要设置该博客关联的用户，否则更新将需要更新的信息补充到原Blog中，原来的其他属性值不变
            if (blog.getId() == null) {
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            } else {
                Blog orignalBlog = blogService.getBlogById(blog.getId());
                orignalBlog.setTitle(blog.getTitle());
                orignalBlog.setContent(blog.getContent());
                orignalBlog.setSummary(blog.getSummary());
                orignalBlog.setCatalog(blog.getCatalog());
                orignalBlog.setTags(blog.getTags());
                blogService.saveBlog(orignalBlog);
            }
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

}
