package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.dto.SetmealDto;
import com.tyl.waimai.entity.Setmeal;
import com.tyl.waimai.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    public Result<Page<Setmeal>> page(int  page,int pageSize,String name){
       Page<Setmeal> pageInfo=new Page<>(page,pageSize);
       LambdaQueryWrapper<Setmeal> wrapper=new LambdaQueryWrapper<>();
       wrapper.eq(name!=null,Setmeal::getName,name);
       setmealService.page(pageInfo,wrapper);
       return Result.success(pageInfo);
    }
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return Result.success("删除成功");
    }
    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return Result.success(list);
    }
}
