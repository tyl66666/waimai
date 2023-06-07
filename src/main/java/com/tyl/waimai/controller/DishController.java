package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.dto.DishDto;
import com.tyl.waimai.entity.Category;
import com.tyl.waimai.entity.Dish;
import com.tyl.waimai.service.CategoryService;
import com.tyl.waimai.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/dish")
public class DishController {

     @Autowired
     private DishService dishService;
     @Autowired
     private CategoryService categoryService;

      @PostMapping
      public Result<String> add(@RequestBody DishDto dishDto){
          dishService.saveWithFlavor(dishDto);
         return Result.success("添加成功","success");
      }
      //仔细看看
     //TODO 改进
      @GetMapping("/page")
      public Result<Page> page(int page, int pageSize, String name){
          Page<Dish> pageInfo=new Page<>(page,pageSize);
          Page<DishDto> dishDtoPage=new Page<>();

          LambdaQueryWrapper<Dish> wrapper=new LambdaQueryWrapper<>();
          wrapper.like(name!=null,Dish::getName,name);
          wrapper.orderByDesc(Dish::getUpdateTime);

          dishService.page(pageInfo,wrapper);

          BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
          List<Dish> records = pageInfo.getRecords();
          List<DishDto> list=records.stream().map((item)->{
              DishDto dishDto=new DishDto();
              BeanUtils.copyProperties(item,dishDto);
              Long categoryId = item.getCategoryId();
              Category category = categoryService.getById(categoryId);
              dishDto.setCategoryName(category.getName());
              return dishDto;
          }).collect(Collectors.toList());

          dishDtoPage.setRecords(list);

          return Result.success(dishDtoPage);
      }


      @GetMapping("/{id}")
      public Result<DishDto> get(@PathVariable("id") Long id){
          DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
          return Result.success(byIdWithFlavor);
      }

    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改成功","success");
    }

    //根据id查询所有菜品
    @GetMapping("/list")
    public Result<List<Dish>> list(Dish dish){
         LambdaQueryWrapper<Dish> wrapper=new LambdaQueryWrapper<>();
         wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
         wrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
         //查询状态正常的菜品
         wrapper.eq(Dish::getStatus,1);
         List<Dish> list = dishService.list(wrapper);
         return Result.success(list);
    }
}
