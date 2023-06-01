package com.tyl.waimai.service.impl;

import com.tyl.waimai.entity.User;
import com.tyl.waimai.mapper.UserMapper;
import com.tyl.waimai.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
