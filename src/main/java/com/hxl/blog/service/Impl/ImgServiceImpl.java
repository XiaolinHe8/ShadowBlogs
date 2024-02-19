package com.hxl.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.dao.ImgMapper;
import com.hxl.blog.dao.TagMapper;
import com.hxl.blog.pojo.Img;
import com.hxl.blog.pojo.Tag;
import com.hxl.blog.service.ImgService;
import com.hxl.blog.util.ImageSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImgServiceImpl implements ImgService {
    @Autowired
    private ImgMapper imgMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public Page<Img> getImg(Page<Img> page, Integer tagId) {
        if(page == null){
            page = new Page<>(1, 20);
        }
        Page<Img> imgs = imgMapper.selectPage(page, new QueryWrapper<Img>()
                .eq("tag_id", tagId).orderByDesc("id"));
        return imgs;
    }

    @Override
    public Page<Img> getImg(Page<Img> page) {
        if(page == null){
            page = new Page<>(1, 40);
        }
        Page<Img> imgs = imgMapper.selectPage(page, null);
        return imgs;
    }

    @Override
    public Img insertImg(Img img) {
        imgMapper.insert(img);
        return img;
    }

    @Override
    public void deleteImg(Integer imgId) {
        Img img = imgMapper.selectById(imgId);
        ImageSave.deleteImg(img.getLocalImg());
        imgMapper.deleteById(imgId);
    }

    @Override
    public List<Tag> listTag(Integer size) {
        List<Tag> tags = tagMapper.listImgTop(size);
        for(int i=0;i<tags.size();i++){
            tags.get(i).setNumber(imgMapper.selectCount(new QueryWrapper<Img>()
                    .eq("tag_id",tags.get(i).getId())));
        }
        return tags;
    }

}
