package com.tyl.waimai.service.impl;

import com.tyl.waimai.entity.Category;
import com.tyl.waimai.mapper.CategoryMapper;
import com.tyl.waimai.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
