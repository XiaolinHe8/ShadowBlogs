package com.hxl.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.dao.*;
import com.hxl.blog.myException.NotFoundException;
import com.hxl.blog.pojo.*;
import com.hxl.blog.service.BlogService;
import com.hxl.blog.util.MarkdownUtils;
import com.hxl.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl  implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogTagMapper blogTagMapper;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public Blog getBlog(Integer id) {
        Blog blog = setBlogInfo(blogMapper.selectById(id));
        return blog;
    }
    private Blog setBlogInfo(Blog blog){
        if(blog == null)
            return null;
        Type type = typeMapper.selectById(blog.getTypeId());
        blog.setType(type);
        blog.setUser(userMapper.selectById(blog.getUserId()));
        blog.setTags(blogTagMapper.listTag(blog.getId()));
        blog.setComments(commentMapper
                .selectList(new QueryWrapper<Comment>()
                        .eq("blog_id",blog.getId())));
        return blog;
    }

    @Override
    public Blog getAndConvert(Integer id) {
        Blog blog = setBlogInfo(blogMapper.selectById(id));
        if(blog == null){
            throw  new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blog.setViews(blog.getViews()+1);
        blogMapper.updateById(blog);
        return b;
    }

    @Override
    public Blog getAndConvert() {
        Blog blog = setBlogInfo(blogMapper.selectOne(new QueryWrapper<Blog>()
                .like("title","关于本站")));
        if(blog == null){
            throw  new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blog.setViews(blog.getViews()+1);
        blogMapper.updateById(blog);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Page<Blog> page, BlogQuery blog) {
        if(page == null){
            page = new Page<Blog>(1,10);
        }
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        if(blog.getTypeId() != null){
            wrapper.eq("type_id",blog.getTypeId());
        }
        if(blog.getTitle() != null && "".equals(blog.getTitle())){
            wrapper.like("title",blog.getTitle());
        }
        if(blog.isRecommend()){
            wrapper.eq("recommend",blog.isRecommend());
        }
        Page<Blog> blogPage = blogMapper.selectPage(page, wrapper);
        List<Blog> blogList = blogPage.getRecords();
        for(int i=0;i<blogList.size();i++){
            setBlogInfo(blogList.get(i));
        }
        blogPage.setRecords(blogList);
        return blogPage;
    }

    @Override
    public Page<Blog> listBlog(Page<Blog> page) {
        if(page == null){
            page = new Page<Blog>(1,10);
        }
        Page<Blog> blogPage = blogMapper.selectPage(page, null);
        List<Blog> blogList = blogPage.getRecords();
        for(int i=0;i<blogList.size();i++){
            setBlogInfo(blogList.get(i));
        }
        blogPage.setRecords(blogList);
        return blogPage;

    }

    @Override
    public Page<Blog> listBlog(Integer tagId, Page<Blog> page) {
        if(page == null){
            page = new Page<Blog>(1,10);
        }
//        List<BlogTag> blogTags = blogTagMapper.selectList(new QueryWrapper<BlogTag>().eq("tag_id", tagId));
////        Page<Blog> blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
////                .eq("id", tagId));
        Page<Blog> blogPage = blogMapper.listPageByTagId(page, tagId);
        List<Blog> blogList = blogPage.getRecords();
        for(int i=0;i<blogList.size();i++){
            setBlogInfo(blogList.get(i));
        }
        blogPage.setRecords(blogList);
        return blogPage;
    }

    @Override
    public Page<Blog> listBlog(String query, Page<Blog> page) {
        if(page == null)
            page = new Page<>(1,10);
        Page<Blog> blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                .like("title", query)
                .or()
                .like("content", query));
        List<Blog> blogList = blogPage.getRecords();
        for(int i=0;i<blogList.size();i++){
            setBlogInfo(blogList.get(i));
        }
        blogPage.setRecords(blogList);
        return blogPage;
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Page<Blog> page = new Page<>(1,size);
        List<Blog> blogList = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                .orderByDesc("update_time")
                .eq("recommend",true))
                .getRecords();
        for(int i=0;i<blogList.size();i++){
            setBlogInfo(blogList.get(i));
        }
        return blogList;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogMapper.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            List<Blog> blogList =blogMapper.selectList(new QueryWrapper<Blog>()
                    .like("year(update_time)",year));
            for(int i=0;i<blogList.size();i++){
                setBlogInfo(blogList.get(i));
            }
            map.put(year, blogList);
        }
        return map;
    }

    @Override
    public Integer countBlog() {
        return blogMapper.selectCount(null);
    }

    @Override
    public Blog insertBlog(Blog blog) {
        if(blog.getId() == null){
            blog.setViews(0);
        }
        blogMapper.insert(blog);
        for (Tag tag :  blog.getTags()) {
            blogTagMapper.insert(new BlogTag()
                    .setBlogId(blog.getId())
                    .setTagId(tag.getId()));
        }
        return blog;
    }

    @Override
    public Blog updateBlog(Integer id, Blog blog) {
        Blog blog1 = blogMapper.selectById(id);
        if(blog1 == null){
            throw  new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,blog1, MyBeanUtils.getNullPropertyNames(blog));
        int i = blogMapper.updateById(blog1);
        Map map = new HashMap<String,Object>();
        map.put("blog_id",id);
        blogTagMapper.deleteByMap(map);
        for (Tag tag :  blog.getTags()) {
            blogTagMapper.insert(new BlogTag()
                    .setBlogId(blog.getId())
                    .setTagId(tag.getId()));
        }
        return blog1;
    }

    @Override
    public void deleteBlog(Integer id) {
        blogMapper.deleteById(id);

    }
}
