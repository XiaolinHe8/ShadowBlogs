package com.hxl.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Tag;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

    @Select("select id,name from tag where id in (select tag_id from (select tag_id from blog_tag group by tag_id order by count(*) desc limit #{size} ) as  t)")
    public List<Tag> listTop(Integer size);

    @Select("select id,name from tag where id in (select tag_id from img where tag.id = tag_id) limit #{size}")
    public List<Tag> listImgTop(Integer size);

}
