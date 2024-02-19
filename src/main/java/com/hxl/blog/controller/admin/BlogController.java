package com.hxl.blog.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Blog;
import com.hxl.blog.pojo.BlogQuery;
import com.hxl.blog.pojo.User;
import com.hxl.blog.service.BlogService;
import com.hxl.blog.service.Impl.BlogServiceImpl;
import com.hxl.blog.service.Impl.TagServiceImpl;
import com.hxl.blog.service.Impl.TypeServiceImpl;
import com.hxl.blog.service.TagService;
import com.hxl.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";


    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TypeServiceImpl typeService;
    @Autowired
    private TagServiceImpl tagService;

    @GetMapping("/blogs")
    public String blogs(Page<Blog> page, BlogQuery blog, Model model) {
        JSONObject json = new JSONObject();
        model.addAttribute("types", typeService.listType());
        json.put("data",blogService.listBlog(page, blog));
        model.addAttribute("page", json);
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(Page<Blog> page,
                         BlogQuery blog, Model model) {
        JSONObject json = new JSONObject();
        json.put("data",blogService.listBlog(page, blog));
        model.addAttribute("page", json);
        return "admin/blogs :: blogList";
    }


    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }


    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Integer id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }



    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUserId(((User) session.getAttribute("user")).getId());
        blog.setTypeId(blog.getType().getId());
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b = null;
        if (blog.getId() == null) {
            b =  blogService.insertBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }


    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Integer id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }



}
