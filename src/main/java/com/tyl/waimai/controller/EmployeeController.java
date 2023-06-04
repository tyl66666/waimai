package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.entity.Employee;
import com.tyl.waimai.service.EmployeeService;
import com.tyl.waimai.utils.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

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


    //登入
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
        if(emp.getStatus().equals(0)){
            return Result.fail("此账号已被注销");
        }

        //将用户id存到redis中
        redisTemplate.opsForValue().set(RedisConstant.EMPLOYEE_ID,emp.getId(),RedisConstant.EMPLOYEE_TTL, TimeUnit.MINUTES);
        return Result.success(emp);
    }

    //退出
    @PostMapping("/logout")
    public Result<String> logout(){
        redisTemplate.delete(RedisConstant.EMPLOYEE_ID);
        return Result.success("退出成功");
    }


    //新增员工
    @PostMapping
    public Result<String> save(@RequestBody Employee employee){

        //判断用户名是否重复
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(wrapper);
        if(emp!=null){
            throw new RuntimeException("用户重复了");
        }

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

        employeeService.save(employee);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){

        //判断查询条件
        LambdaQueryWrapper<Employee> wrapper=new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Employee::getName,name);
        wrapper.orderByDesc(Employee::getUpdateTime);


        Page<Employee> pageInfo=new Page(page,pageSize);

        employeeService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @PutMapping
    public Result<String> update(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return Result.success("更新成功");
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return Result.success(employee);
        }
        return  Result.fail("查无此人");
    }

}
