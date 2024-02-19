package com.hxl.blog.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Img;
import com.hxl.blog.pojo.Tag;
import com.hxl.blog.service.Impl.ImgServiceImpl;
import com.hxl.blog.service.Impl.TagServiceImpl;
import com.hxl.blog.util.ImageSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ImageController {
    @Autowired
    private ImgServiceImpl imgService;
    @Autowired
    private ImageSave imageSave;
    @Autowired
    private TagServiceImpl tagService;


    @GetMapping("/img/tags/{id}")
    public String getImage(@PathVariable Integer id,Page<Img> page,Model model){
        List<Tag> tag = tagService.listTag();
        List<Tag> tags = imgService.listTag(1000);
        if(id == -1){
            if(tags.size() > 0){
                id = tags.get(0).getId();
            }
        }
        Page<Img> img = imgService.getImg(page,id);
        model.addAttribute("tags",tags);
        model.addAttribute("allTags",tag);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("data",img);
        model.addAttribute("imgs",jsonObject1);
        model.addAttribute("activeTagId", id);
        return "admin/image";
    }



    @PostMapping("/uploadImg")
    @ResponseBody
    public Img saveImage(RedirectAttributes attributes,HttpServletRequest req, @RequestPart MultipartFile files,Img img){
        List<String> list = imageSave.utils(req, files);
        img.setLocalImg(list.get(0));
        img.setVirtualImg(list.get(1));
        imgService.insertImg(img);
        return  img;
    }

    @GetMapping("/img/{id}/delete")
    public String deleteImage(@PathVariable Integer id, RedirectAttributes attributes){
        imgService.deleteImg(id);
        attributes.addFlashAttribute("message", "删除成功");
       return  "redirect:/admin/img/tags/-1";
    }

}
