package com.tyl.waimai.dto;


import com.tyl.waimai.entity.Dish;
import com.tyl.waimai.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
