package com.litj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.litj.reggie.dto.DishDto;
import com.litj.reggie.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {

    public void saveDishWithFlavor(DishDto dishDto);

    public DishDto getByDishIdWithFlavor(long dishId);

    public void updateDishWithFlavor(DishDto dishDto);

    public void deleteDishWithFlavor(List<Long> dishIds);


}
