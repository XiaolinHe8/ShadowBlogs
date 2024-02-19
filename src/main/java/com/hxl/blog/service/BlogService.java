package com.hxl.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Blog;
import com.hxl.blog.pojo.BlogQuery;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Integer id);

    Page<Blog> listBlog(Page<Blog> page, BlogQuery blog);

    Blog insertBlog(Blog blog);

    Blog updateBlog(Integer id,Blog blog);

    void deleteBlog(Integer id);

    Blog getAndConvert(Integer id);

    Blog getAndConvert();

    Page<Blog> listBlog(Page<Blog> page);

    Page<Blog> listBlog(Integer tagId,Page<Blog> page);

    Page<Blog> listBlog(String query,Page<Blog> page);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Integer countBlog();


}
