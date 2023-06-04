package com.tyl.waimai.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tyl.waimai.utils.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

@Configuration
@Slf4j

/**
 * 自动填充
 */
public class MyMetaObjecthandler implements MetaObjectHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void insertFill(MetaObject metaObject) {

        Long id = Long.valueOf(redisTemplate.opsForValue().get(RedisConstant.EMPLOYEE_ID).toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long id = Long.valueOf(redisTemplate.opsForValue().get(RedisConstant.EMPLOYEE_ID).toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", id);
    }
}
