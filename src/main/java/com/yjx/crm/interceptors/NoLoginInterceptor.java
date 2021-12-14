package com.yjx.crm.interceptors;


import com.yjx.crm.exceptions.NoLoginException;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* 非法访问拦截
* */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (userId == null || userService.selectByPrimaryKey(userId) == null){
            throw new NoLoginException("用户未登录");
        }
        return true;
    }

}
