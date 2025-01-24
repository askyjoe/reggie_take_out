package com.litj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.litj.reggie.common.CustomException;
import com.litj.reggie.common.R;
import com.litj.reggie.dto.DishDto;
import com.litj.reggie.entity.*;
import com.litj.reggie.service.CategoryService;
import com.litj.reggie.service.DishFlavorService;
import com.litj.reggie.service.DishService;
import com.litj.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        dishService.saveDishWithFlavor(dishDto);
        return R.success("success");

    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {

        return R.success(dishService.getByDishIdWithFlavor(id));
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {

        dishService.updateDishWithFlavor(dishDto);
        return R.success("success");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids!=null, Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, status);
        dishService.update(updateWrapper);

        return R.success("success");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, 1);
        if(dishService.count(queryWrapper)>0){
            throw new CustomException("Dish on sell cannot be deleted.");
        }
        dishService.deleteDishWithFlavor(ids);

        return R.success("success");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Dish> dishPageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(dishPageInfo, queryWrapper);

        Page<DishDto> dishDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(dishPageInfo, dishDtoPageInfo, "records");

        List<Dish> records = dishPageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(dishDtoList);

        return R.success(dishDtoPageInfo);
    }

//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus, 1);
//        queryWrapper.orderByAsc(Dish::getSort);
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//
//        return R.success(dishService.list(queryWrapper));
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> dishList = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
            qw.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList =  dishFlavorService.list(qw);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        //return R.success(dishService.list(queryWrapper));
        return R.success(dishDtoList);
    }
}
