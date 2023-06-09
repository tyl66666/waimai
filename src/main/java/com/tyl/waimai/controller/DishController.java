package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.dto.DishDto;
import com.tyl.waimai.entity.Category;
import com.tyl.waimai.entity.Dish;
import com.tyl.waimai.entity.DishFlavor;
import com.tyl.waimai.service.CategoryService;
import com.tyl.waimai.service.DishFlavorService;
import com.tyl.waimai.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
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

     @Autowired
     private DishFlavorService dishFlavorService;

     @Autowired(required = false)
     private RedisTemplate  redisTemplate;

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
          //删除缓存
          String key="dish_"+dishDto.getCategoryId()+"_1";
          redisTemplate.delete(key);
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改成功","success");
    }

//    //根据id查询所有菜品
//    @GetMapping("/list")
//    public Result<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        wrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        //查询状态正常的菜品
//        wrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(wrapper);
//        return Result.success(list);
//    }

    //根据id查询所有菜品
    //写完再看看
    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList=null;

          String key="dish_"+dish.getCategoryId()+"_"+ dish.getStatus();

         dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
         if(!StringUtils.isEmpty(dishDtoList)){
             return Result.success(dishDtoList);
         }

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

    //添加排序条件
      queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

       List<Dish> list = dishService.list(queryWrapper);

       dishDtoList = list.stream().map((item) -> {
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(item,dishDto);

        Long categoryId = item.getCategoryId();//分类id
        //根据id查询分类对象
        Category category = categoryService.getById(categoryId);

        if(category != null){
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
        }

        //当前菜品的id
        Long dishId = item.getId();
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
        //SQL:select * from dish_flavor where dish_id = ?
        List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }).collect(Collectors.toList());


       redisTemplate.opsForValue().set(key,dishDtoList);
       return Result.success(dishDtoList);
    }
}
