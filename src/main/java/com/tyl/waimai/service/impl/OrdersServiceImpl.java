package com.tyl.waimai.service.impl;

import com.tyl.waimai.entity.Orders;
import com.tyl.waimai.mapper.OrdersMapper;
import com.tyl.waimai.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
