package com.litj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.litj.reggie.common.R;
import com.litj.reggie.entity.User;
import com.litj.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 发送验证码
        return R.success("success");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        // 匹配验证码
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getPhone, map.get("phone").toString());
        User user = userService.getOne(queryWrapper);
        if(user == null) {
            user = new User();
            user.setPhone(map.get("phone").toString());
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user", user.getId());

        return R.success(user);
    }
}
