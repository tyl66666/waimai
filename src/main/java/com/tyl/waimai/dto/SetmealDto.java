package com.tyl.waimai.dto;


import com.tyl.waimai.entity.Setmeal;
import com.tyl.waimai.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
