package com.tyl.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.entity.User;
import com.tyl.waimai.service.UserService;
import com.tyl.waimai.utils.RedisConstant;
import com.tyl.waimai.utils.SMSUtils;
import com.tyl.waimai.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    //TODO 由于前端没有写这个请求 以后自己天机
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user){
        //获取手机号
        String phone = user.getPhone();
        //判断phone是否为空
        if(!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //发送短息
            //SMSUtils.sendMessage("小吉外卖","SMS_461145035",user.getPhone(),code);

            //将验证码放到redis中
            redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_CODE+phone,code,RedisConstant.USER_LOGIN_CODE_TTL, TimeUnit.MINUTES);

        }
        return Result.success("发送成功","success");
    }

    //TDOO 由于现在前端不行 没有传验证码
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map){
        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码

        //从redis中取出验证码 进行比对
//        Object codeInSession = redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_CODE + phone);
         String codeInSession="1";
        //如果比对成功 则可登入
         if(codeInSession!=null&&codeInSession.equals("1")){
             //判断当前是否为新用户 如果是则创建
             LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
             wrapper.eq(User::getPhone,phone);

             User use = userService.getOne(wrapper);
             if(StringUtils.isEmpty(use)){
                 User u=new User();
                 u.setPhone(phone);
                 u.setStatus(1);
                 userService.save(u);
             }

             redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_ID, use.getId());

//             if(use.getStatus().equals(0)){
//                 throw new RuntimeException("此账号已注销");
//             }

             return Result.success(use);
         }

        return Result.success("登入失败");
    }

}
