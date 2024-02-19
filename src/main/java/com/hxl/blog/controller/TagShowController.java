package com.hxl.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Blog;
import com.hxl.blog.pojo.Tag;
import com.hxl.blog.service.Impl.BlogServiceImpl;
import com.hxl.blog.service.Impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping("/tags/{id}")
    public String tags(Page<Blog> page, @PathVariable Integer id, Model model) {
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        JSONObject blog = new JSONObject();
        blog.put("data",blogService.listBlog(id, page));
        model.addAttribute("tags", tags);
        model.addAttribute("page",blog );
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
