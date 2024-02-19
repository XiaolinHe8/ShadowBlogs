package com.hxl.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxl.blog.pojo.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
