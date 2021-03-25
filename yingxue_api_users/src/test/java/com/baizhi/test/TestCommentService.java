package com.baizhi.test;

import com.baizhi.entity.Comment;
import com.baizhi.service.CommentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestCommentService  extends BasicTest{

    @Autowired
    private CommentService commentService;


    @Test
    public void test(){
        List<Comment> comments = commentService.findByVideoId(11,1,2);
        comments.forEach(comment->{
            System.out.println(comment);
            List<Comment> children = comment.getChildren();
            for (Comment child : children) {
                System.out.println(">>>>>>>>>>>>"+child);
                List<Comment> children1 = child.getChildren();
                for (Comment comment1 : children1) {
                    System.out.println("--------------------->"+comment1);
                }
            }
        });
    }
}
