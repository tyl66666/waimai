package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.entity.Employee;
import com.tyl.waimai.service.EmployeeService;
import com.tyl.waimai.utils.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired(required = false)
    private EmployeeService employeeService;

    @Autowired
    private RedisTemplate redisTemplate;



    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee){
        //获取前端传过来的密码 经行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        //根据账号 密码比对
        LambdaQueryWrapper<Employee> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername()).eq(Employee::getPassword,password);
        Employee emp = employeeService.getOne(wrapper);

        //判断employee1 是否为空
        if(emp==null){
            return Result.fail("账号或密码错误");
        }

        //判断登入状态
        if(emp.getId().equals(0)){
            return Result.fail("此账号已被注销");
        }

        //将用户id存到redis中
        redisTemplate.opsForValue().set(RedisConstant.EMPLOYEE_ID,emp.getId());
        return Result.success(emp);
    }





}
