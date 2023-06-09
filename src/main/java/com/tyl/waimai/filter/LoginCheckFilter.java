package com.tyl.waimai.filter;

import com.alibaba.fastjson.JSON;
import com.tyl.waimai.common.BaseContext;
import com.tyl.waimai.common.Result;
import com.tyl.waimai.utils.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登入(使用过滤器)
 * TODO 后面可以使用框架
 */
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpServletResponse httpresponse = (HttpServletResponse) response;

        //获得前端的请求
        String requestURI = httprequest.getRequestURI();
        //需要直接放行的路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        boolean check = check(urls, requestURI);

        if(check){
            chain.doFilter(request,response);
            return;
        }

        //TODO 还是得用ThreadLocal装
        Object o = redisTemplate.opsForValue().get(RedisConstant.EMPLOYEE_ID);
        if(o!=null&&o!=""){
            chain.doFilter(request,response);
            return;
        }

        //TODO 写个前端登录过滤器
        Long o1 = (Long)redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_ID);
        if(o1!=null){
            BaseContext.setCurrentId(o1);
            chain.doFilter(request,response);
            return;
        }


        response.getWriter().write(JSON.toJSONString(Result.fail("NOTLOGIN")));
    }

    /**
     * 匹配路径
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
