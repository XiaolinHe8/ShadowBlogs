package com.hxl.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Blog;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogMapper extends BaseMapper<Blog> {

    @Select("select year(update_time) as year from blog b group by year(update_time) order by year desc ")
    List<String> findGroupYear();

//    @Select("select b from Blog b where function('date_format',b.updateTime,'%Y') = #{year}")
//    List<Blog> findByYear(String year);

    @Select("select * from blog where id in (select blog_id from blog_tag where tag_id = #{tagId})")
    Page<Blog> listPageByTagId(Page<Blog> page, Integer tagId);
}
