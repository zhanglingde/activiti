package com.mashibing.activiti.web.module.system.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 孙志强
 * @since 2020-04-13
 */
@RestController
public class UserController {

    @RequestMapping("/user/add")
    public void add(){
        System.out.println("拥有权限：" + "user:add");
    }
    @RequestMapping("/user/edit")
    public void edit(){
        add();
        System.out.println("拥有权限：" + "user:edit");
    }
}
