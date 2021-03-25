package com.baizhi.controller;

import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类(Category)表控制层
 *
 * @author xiaochen
 * @since 2021-03-19 10:33:11
 */



@RestController
public class CategoryController {
    /**
     * 服务对象
     */
    @Resource
    private CategoryService categoryService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("categories/selectOne")
    public Category selectOne(Integer id) {
        return this.categoryService.queryById(id);
    }


    //查询所有  查询一级类别
    @GetMapping("categories")
    public List<Category> categories(){
        return categoryService.findAll();
    }

    //添加类别  header: application/json body  {}
    @PostMapping("categories")
    public void save(@RequestBody Category category){
        this.categoryService.insert(category);
    }


    //删除类别
    @DeleteMapping("/categories/{id}")
    public void delete(@PathVariable("id") Integer id){
        this.categoryService.deleteById(id);
    }


    //修改类别
    @PatchMapping("/categories/{id}")
    public Category update(@PathVariable("id") Integer id,@RequestBody  Category category){
        category.setId(id);
        return this.categoryService.update(category);
    }
}
