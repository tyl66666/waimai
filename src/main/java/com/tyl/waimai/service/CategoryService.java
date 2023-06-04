package com.tyl.waimai.service;

import com.tyl.waimai.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
