package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.entity.OrderDetail;
import com.tyl.waimai.entity.Orders;
import com.tyl.waimai.service.OrderDetailService;
import com.tyl.waimai.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return Result.success("下单成功");
    }

    //TODO  封装的数据有点问题
    @GetMapping("/userPage")
    public Result<Page>  page(Integer page,Integer pageSize){

        Page<OrderDetail> pageInfo=new Page<>(page,pageSize);
        orderDetailService.page(pageInfo);
        return Result.success(pageInfo);

    }


}
