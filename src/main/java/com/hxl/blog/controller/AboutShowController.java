package com.hxl.blog.controller;

import com.hxl.blog.pojo.User;
import com.hxl.blog.service.Impl.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AboutShowController {

    @Autowired
    private BlogServiceImpl blogService;
    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        if(session.getAttribute("user") != null){
            model.addAttribute("admin", (User)session.getAttribute("user"));
        }
        model.addAttribute("blog", blogService.getAndConvert());
        return "blog";
    }
}