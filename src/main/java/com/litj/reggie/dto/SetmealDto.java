package com.litj.reggie.dto;

import com.litj.reggie.entity.Setmeal;
import com.litj.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
