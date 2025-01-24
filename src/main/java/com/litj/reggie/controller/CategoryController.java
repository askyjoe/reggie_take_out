package com.litj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.litj.reggie.common.R;
import com.litj.reggie.entity.Category;
import com.litj.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("save success");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        categoryService.remove(ids);
        return R.success("delete success");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("update success");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        List<Category> list = categoryService.list(
                new LambdaQueryWrapper<Category>().eq(category.getType()!=null,
                        Category::getType, category.getType()).orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime)
        );
        return R.success(list);
    }
}
