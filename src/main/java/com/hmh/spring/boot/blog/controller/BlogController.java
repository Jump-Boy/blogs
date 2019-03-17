package com.hmh.spring.boot.blog.controller;

import com.hmh.spring.boot.blog.domain.EsBlog;
import com.hmh.spring.boot.blog.domain.User;
import com.hmh.spring.boot.blog.service.EsBlogService;
import com.hmh.spring.boot.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Blog控制器（包含搜索博客及展示的相关接口）
 *
 * @author hmh
 * @date 2019/3/5
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    /**
     * 博客搜索：默认筛选出最新的博客，可以指定排序条件与筛选关键字。重定向到首页，搜索功能由首页完成，因为默认进入首页，
     * 就会展示出默认的博客内容。
     *
     * @author hmh
     * @date 2019/3/12
     * @param order
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return java.lang.String
     */
    @GetMapping
    public String listEsBlogs(
            @RequestParam(value="order",required=false,defaultValue="new") String order,
            @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            Model model) {

        Page<EsBlog> page = null;
        List<EsBlog> list = null;
        //系统初始化时，没有博客数据
        boolean isEmpty = true;
        try {
            if (order.equals("hot")) {
                //最热查询
                Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
                Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
                page = esBlogService.listHotestEsBlogs(keyword, pageable);
            } else if (order.equals("new")) {
                //最新查询（创建时间逆序）
                Sort sort = new Sort(Sort.Direction.DESC,"createTime");
                Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
                page = esBlogService.listNewestEsBlogs(keyword, pageable);
            }
            isEmpty = false;
        } catch (Exception e) {
            //系统初始化时，ES中可能没有数据，上头的查询可能会报错，为了不影响页面显示，这里捕获进行处理
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = esBlogService.listEsBlogs(pageable);
        }
        //当前所在页面数据列表
        list = page.getContent();

        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        /*
         * 首次访问页面才加载（首次访问页面，async为false，同时在es有数据的情况下，去获取最热最新的标签用户等数据，然后
         * 填充渲染到首页的右侧。当进行翻页时，即async为true，只需页面中间数据再加载就行，右侧数据无需重新加载）
         */
        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest", newest);
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest", hotest);
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("users", users);
        }
        return (async == true ? "/index :: #mainContainerRepleace" : "/index");
    }

    /**
     * 最新的博客（5篇）
     *
     * @author hmh
     * @date 2019/3/12
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/newest")
    public String listNewestEsBlogs(Model model) {
        List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
        model.addAttribute("newest", newest);
        return "newest";
    }

    /**
     * 最热的博客（5篇）
     *
     * @author hmh
     * @date 2019/3/12
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/hotest")
    public String listHotestEsBlogs(Model model) {
        List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
        model.addAttribute("hotest", hotest);
        return "hotest";
    }

}
