package com.hxl.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxl.blog.dao.CommentMapper;
import com.hxl.blog.pojo.Comment;
import com.hxl.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> listCommentByBlogId(Integer blogId) {
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("blog_id", blogId)
                .isNull("parent_id")
                .orderByDesc("create_time"));
        getCommentInfo(comments);
        return eachComment(comments);
    }


    @Override
    public Comment insertComment(Comment comment) {
        Integer parentCommentId = comment.getParentId();
        if (parentCommentId == -1) {
            comment.setParentId(null);
        }
        commentMapper.insert(comment);
        return comment;
    }

    private void getCommentInfo(List<Comment> comments){
        for(int i=0;i<comments.size();i++){
            comments.get(i)
                    .setReplyComments(commentMapper
                            .selectList(new QueryWrapper<Comment>()
                                    .eq("parent_id",comments.get(i).getId())));
            comments.get(i).setParentComment(commentMapper
                    .selectOne(new QueryWrapper<Comment>()
                            .eq("id",comments.get(i).getParentId())));
        }
    }

    private void getCommentInfo(Comment comment){
            comment.setReplyComments(commentMapper
                            .selectList(new QueryWrapper<Comment>()
                                    .eq("parent_id",comment.getId())));
            comment.setParentComment(commentMapper
                    .selectOne(new QueryWrapper<Comment>()
                            .eq("id",comment.getParentId())));
    }
    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        combineChildren(comments);
        return comments;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            getCommentInfo(replys1);
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        getCommentInfo(comment);
        tempReplys.add(comment);//顶节点添加到临时存放集合

        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            getCommentInfo(replys);
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
}
