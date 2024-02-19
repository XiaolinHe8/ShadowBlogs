package com.hxl.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Tag;

import java.util.List;

public interface TagService {
    Tag insertTag(Tag type);

    Tag getTag(Integer id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Page<Tag> page);


    List<Tag> listTag();

    List<Tag> listTagTop(Integer size);

    List<Tag> listTag(String id);

    Tag updateTag(Integer id, Tag type);

    void deleteTag(Integer id);
}
