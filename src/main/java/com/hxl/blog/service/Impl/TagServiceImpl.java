package com.hxl.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.dao.BlogTagMapper;
import com.hxl.blog.dao.TagMapper;
import com.hxl.blog.myException.NotFoundException;
import com.hxl.blog.pojo.Blog;
import com.hxl.blog.pojo.BlogTag;
import com.hxl.blog.pojo.Tag;
import com.hxl.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogTagMapper blogTagMapper;
    @Override
    public Tag insertTag(Tag tag) {
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public Tag getTag(Integer id) {
        return tagMapper.selectById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagMapper.selectOne(new QueryWrapper<Tag>().eq("name",name));
    }

    @Override
    public Page<Tag> listTag(Page<Tag> page) {
        if(page == null){
            page = new Page<Tag>(1,10);
        }
        return tagMapper.selectPage(page,new QueryWrapper<Tag>().orderByDesc("id"));
    }


    @Override
    public List<Tag> listTag() {
        return tagMapper.selectList(null);
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        List<Tag> tags = tagMapper.listTop(size);
        for(int i=0;i<tags.size();i++){
            tags.get(i).setNumber(blogTagMapper
                    .selectCount(new QueryWrapper<BlogTag>()
                            .eq("tag_id",tags.get(i).getId())));
        }
        return tags;
    }



    @Override
    public List<Tag> listTag(String ids) {
        return tagMapper.selectList(new QueryWrapper<Tag>()
                .in("id",convertToList(ids)));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list =null;
        if (!"".equals(ids) && ids != null) {
            list =  new ArrayList<>();
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Override
    public Tag updateTag(Integer id, Tag tag) {
        Tag t = tagMapper.selectById(id);
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag,t);
        tagMapper.updateById(t);
        return t;
    }

    @Override
    public void deleteTag(Integer id) {
        tagMapper.deleteById(id);
    }
}
