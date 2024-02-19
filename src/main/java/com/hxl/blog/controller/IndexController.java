package com.hxl.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Blog;
import com.hxl.blog.pojo.User;
import com.hxl.blog.service.Impl.BlogServiceImpl;
import com.hxl.blog.service.Impl.TagServiceImpl;
import com.hxl.blog.service.Impl.TypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private TypeServiceImpl typeService;

    @Autowired
    private TagServiceImpl tagService;

    @GetMapping("/")
    public String index(Page<Blog> page,
                        Model model) {
        JSONObject blog = new JSONObject();
        blog.put("data",blogService.listBlog(page));
        model.addAttribute("page",blog);
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }


    @PostMapping("/search")
    public String search(Page<Blog> page, @RequestParam String query, Model model) {
        JSONObject blog = new JSONObject();
        blog.put("data",blogService.listBlog(query, page));
        model.addAttribute("page",blog);
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Integer id, Model model, HttpSession session) {
        if(session.getAttribute("user") != null){
            model.addAttribute("admin", (User)session.getAttribute("user"));
        }
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
