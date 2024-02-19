package com.hxl.blog.service;

import com.hxl.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Integer blogId);

    Comment insertComment(Comment comment);
}
