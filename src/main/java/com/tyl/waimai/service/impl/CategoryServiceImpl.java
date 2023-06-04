package com.tyl.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyl.waimai.entity.Category;
import com.tyl.waimai.entity.Dish;
import com.tyl.waimai.entity.Setmeal;
import com.tyl.waimai.mapper.CategoryMapper;
import com.tyl.waimai.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyl.waimai.service.DishService;
import com.tyl.waimai.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    @Override
    public void remove(Long id) {
        //判断菜品分类中是否还有菜
        LambdaQueryWrapper<Dish> dishwrapper=new LambdaQueryWrapper<Dish>();
        dishwrapper.eq(Dish::getCategoryId,id);

        int count = dishService.count(dishwrapper);
        if(count>0){
            throw new RuntimeException("无法删除");
        }

        //判断套餐分类中是否还有套餐
        LambdaQueryWrapper<Setmeal> setmealwrapper=new LambdaQueryWrapper<Setmeal>();
        setmealwrapper.eq(Setmeal::getCategoryId,id);

        int count1 = setmealService.count(setmealwrapper);
        if(count1>0){
            throw new RuntimeException("无法删除");
        }
        super.removeById(id);
    }
}
