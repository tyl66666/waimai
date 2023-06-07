package com.tyl.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyl.waimai.dto.SetmealDto;
import com.tyl.waimai.entity.Setmeal;
import com.tyl.waimai.entity.SetmealDish;
import com.tyl.waimai.mapper.SetmealMapper;
import com.tyl.waimai.service.SetmealDishService;
import com.tyl.waimai.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //先保存到setmeal表中
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.save(setmeal);

        //在加入到setmeal_dish表中
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmeal.getId().toString());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        //TODO 可以改进逻辑
        //查询套餐状态，确定是否可用
        LambdaQueryWrapper<Setmeal> wrapper=new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);

        int count = this.count(wrapper);
        if(count>0){
            throw new RuntimeException("某套餐正在买，无法删除");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> wrapper1=new LambdaQueryWrapper<>();
        wrapper1.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(wrapper1);
    }
}
