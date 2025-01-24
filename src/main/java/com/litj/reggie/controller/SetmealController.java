package com.litj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.litj.reggie.common.CustomException;
import com.litj.reggie.common.R;
import com.litj.reggie.dto.SetmealDto;
import com.litj.reggie.entity.Category;
import com.litj.reggie.entity.Setmeal;
import com.litj.reggie.service.CategoryService;
import com.litj.reggie.service.SetmealDishService;
import com.litj.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveSetmealWithDish(setmealDto);
        return R.success("succcess");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids!=null, Setmeal::getId, ids);
        updateWrapper.set(Setmeal::getStatus, status);
        setmealService.update(updateWrapper);

        return R.success("success");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        if(setmealService.count(queryWrapper)>0){
            throw new CustomException("Setmeal on sell cannot be deleted.");
        }
        setmealService.deleteSetmealWithDish(ids);

        return R.success("success");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Setmeal> setmealPageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(setmealPageInfo, queryWrapper);

        Page<SetmealDto> setmealDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(setmealPageInfo, setmealDtoPageInfo);

        List<Setmeal> records = setmealPageInfo.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPageInfo.setRecords(setmealDtoList);

        return R.success(setmealDtoPageInfo);
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {


        List<Setmeal> list = setmealService.list(
                new LambdaQueryWrapper<Setmeal>().eq(setmeal.getCategoryId()!=null, Setmeal::getCategoryId, setmeal.getCategoryId()
                ).eq(setmeal.getStatus()!=null, Setmeal::getStatus, setmeal.getStatus()).orderByDesc(Setmeal::getUpdateTime)
        );
        return R.success(list);
    }
}
