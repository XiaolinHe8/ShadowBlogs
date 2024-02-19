package com.hxl.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Blog;
import com.hxl.blog.pojo.BlogQuery;
import com.hxl.blog.pojo.Type;
import com.hxl.blog.service.BlogService;
import com.hxl.blog.service.Impl.BlogServiceImpl;
import com.hxl.blog.service.Impl.TypeServiceImpl;
import com.hxl.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeServiceImpl typeService;

    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping("/types/{id}")
    public String types( Page<Blog> page, @PathVariable Integer id, Model model) {
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1) {
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);
        JSONObject blog = new JSONObject();
        blog.put("data",blogService.listBlog(page, blogQuery));
        model.addAttribute("page", blog);
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
