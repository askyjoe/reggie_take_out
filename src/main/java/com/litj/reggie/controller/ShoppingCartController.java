package com.litj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.litj.reggie.common.BaseContext;
import com.litj.reggie.common.R;
import com.litj.reggie.entity.ShoppingCart;
import com.litj.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        //当前购物车所属用户ID
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        // 菜品or套餐（下面两个if只能走一个）
        if(shoppingCart.getDishId() != null){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        if(shoppingCart.getSetmealId() != null){
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        // 当前菜品or套餐是否已在购物车
        ShoppingCart sc = shoppingCartService.getOne(queryWrapper);
        if(sc != null){
            sc.setNumber(sc.getNumber() + 1);
            shoppingCartService.updateById(sc);
        }else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            sc = shoppingCart;
        }

        return R.success(sc);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        //当前购物车所属用户ID
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        // 菜品or套餐（下面两个if只能走一个）
        if(shoppingCart.getDishId() != null){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        if(shoppingCart.getSetmealId() != null){
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        // 当前菜品or套餐是否已在购物车
        ShoppingCart sc = shoppingCartService.getOne(queryWrapper);
        sc.setNumber(sc.getNumber() - 1);
        shoppingCartService.updateById(sc);
        if(sc.getNumber() < 1){
            shoppingCartService.remove(queryWrapper);
        }

        return R.success(sc);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        return R.success(shoppingCartService.list(queryWrapper));
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("success");
    }

}
