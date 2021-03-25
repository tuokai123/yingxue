package com.baizhi.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 评论(Comment)实体类
 *
 * @author xiaochen
 * @since 2021-03-18 15:17:39
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment implements Serializable {
    private static final long serialVersionUID = -78459728869907064L;

    private Integer id;
    /**
     * 用户id
     */
    private Integer uid;
    /**
     * 视频id
     */
    private Integer videoId;
    /**
     * 内容
     */
    private String content;
    /**
     * 父评论id
     */
    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Date deletedAt;


    //孩子评论
    private List<Comment> children;


    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

}
