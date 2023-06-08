package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyl.waimai.common.BaseContext;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.entity.ShoppingCart;
import com.tyl.waimai.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
       LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();
       wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
       wrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //获取user_id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);

        //判断是套餐还是dish
        Long dishId = shoppingCart.getDishId();
        if(StringUtils.isEmpty(dishId)){
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else {
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }

        ShoppingCart sc = shoppingCartService.getOne(wrapper);
        //判断是已有 还是新添
        if(StringUtils.isEmpty(sc)){
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            sc=shoppingCart;
        }else {
            Integer number = sc.getNumber();
            sc.setNumber(number+1);
            shoppingCartService.updateById(sc);
        }

        return Result.success(sc);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(){
        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(wrapper);

        return Result.success("清空购物车成功");
    }

    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody  ShoppingCart shoppingCart){

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();

        if(StringUtils.isEmpty(dishId)){
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else {
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        ShoppingCart one = shoppingCartService.getOne(wrapper);

        Integer number = one.getNumber();
        one.setNumber(number-1);

        shoppingCartService.updateById(one);
        return Result.success(one);
    }


}
