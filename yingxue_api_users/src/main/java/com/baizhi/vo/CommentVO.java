package com.baizhi.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class CommentVO {

    private Integer id;

    private String content;

    @JsonProperty("created_at")
    private Date createdAt;

    //评论人
    private Reviewer reviewer;


    @JsonProperty("sub_comments")
    private List<CommentVO> subComments;//子评论内容

    public List<CommentVO> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<CommentVO> subComments) {
        this.subComments = subComments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Reviewer getReviewer() {
        return reviewer;
    }

    public void setReviewer(Reviewer reviewer) {
        this.reviewer = reviewer;
    }
}
