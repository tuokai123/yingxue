package com.baizhi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 分类(Category)实体类
 *
 * @author xiaochen
 * @since 2021-03-19 10:33:10
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)// 用在类上  作用:可以控制在将对象转为json格式字符串时指定当属性值为null 不参与json转换
public class Category implements Serializable {
    private static final long serialVersionUID = -12085114424432414L;

    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父级分类id
     */
    @JsonProperty("parent_id") //修饰在属性上:  作用: 用来指定当前属性在转换的json格式中名称是谁
    private Integer parentId;

    @JsonProperty("created_at")

    private Date createdAt;

    @JsonProperty("updated_at")
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Date deletedAt;


    //定义关系属性
    private List<Category> children;

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
