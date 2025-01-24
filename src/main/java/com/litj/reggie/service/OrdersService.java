package com.litj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.litj.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {

    public void submitOrder(Orders order);
}
