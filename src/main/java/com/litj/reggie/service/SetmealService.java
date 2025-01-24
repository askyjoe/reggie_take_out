package com.litj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.litj.reggie.dto.SetmealDto;
import com.litj.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveSetmealWithDish(SetmealDto setmealDto);
    public void deleteSetmealWithDish(List<Long> setmealIds);
}
