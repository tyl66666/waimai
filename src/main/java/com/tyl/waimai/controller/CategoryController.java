package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.entity.Category;
import com.tyl.waimai.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody Category category){
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName,category.getName());
        Category cate = categoryService.getOne(wrapper);

        //判断是否已存在
        if(!StringUtils.isEmpty(cate)){
            throw new RuntimeException(category.getName()+"以存在哦");
        }

        boolean flag = categoryService.save(category);
        if(flag){
            return Result.success("添加成功");
        }
        return Result.fail("添加失败");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize){

        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Category::getSort);

        Page<Category> pageInfo=new Page<>(page,pageSize);
        categoryService.page(pageInfo,wrapper);

       return Result.success(pageInfo);
    }

     @DeleteMapping
     public Result<String> delete(@RequestParam("ids") Long id){
         categoryService.remove(id);
         return Result.success("删除成功");
     }

     @PutMapping
     public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("修改成功");
     }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }

}
