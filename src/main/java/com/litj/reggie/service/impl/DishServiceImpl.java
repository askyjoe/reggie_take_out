package com.litj.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.litj.reggie.dto.DishDto;
import com.litj.reggie.entity.Dish;
import com.litj.reggie.entity.DishFlavor;
import com.litj.reggie.mapper.DishMapper;
import com.litj.reggie.service.DishFlavorService;
import com.litj.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    @Override
    @Transactional
    public void saveDishWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long id = dishDto.getId();
        List<DishFlavor> dishFlavorList =  dishDto.getFlavors();
        dishFlavorList =  dishFlavorList.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishFlavorList);
    }

    @Override
    public DishDto getByDishIdWithFlavor(long dishId) {

        Dish dish = this.getById(dishId);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavorList);

        return dishDto;

    }

    @Override
    @Transactional
    public void updateDishWithFlavor(DishDto dishDto) {

        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> dishFlavorList =  dishDto.getFlavors();
        dishFlavorList =  dishFlavorList.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavorList);
    }

    @Override
    @Transactional
    public void deleteDishWithFlavor(List<Long> dishIds) {
        this.removeByIds(dishIds);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, dishIds);
        dishFlavorService.remove(queryWrapper);
    }
}
