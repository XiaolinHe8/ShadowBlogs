package com.hxl.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Img;
import com.hxl.blog.pojo.Tag;

import java.util.List;

public interface ImgService {

    public Page<Img> getImg(Page<Img> page,Integer tagId);
    public Page<Img> getImg(Page<Img> page);
    public Img insertImg(Img img);
    public void deleteImg(Integer imgId);
    public List<Tag> listTag(Integer size);
}
