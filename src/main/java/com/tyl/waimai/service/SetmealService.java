package com.tyl.waimai.service;

import com.tyl.waimai.dto.SetmealDto;
import com.tyl.waimai.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
public interface SetmealService extends IService<Setmeal> {

     public void saveWithDish(SetmealDto setmealDto);
     public void removeWithDish(List<Long> ids);
}
