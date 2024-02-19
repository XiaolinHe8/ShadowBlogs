package com.hxl.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxl.blog.pojo.BlogTag;
import com.hxl.blog.pojo.Tag;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    @Select("select tag.id,tag.name from tag,blog_tag where tag.id=tag_id and blog_id = #{blogId} group by tag.id,tag.name")
    public List<Tag> listTag(Integer blogId);

}
