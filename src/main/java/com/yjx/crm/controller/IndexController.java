package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.bean.User;
import com.yjx.crm.service.PermissionService;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest request){
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.setAttribute("user",user);
        List<String> list = permissionService.queryUserHasRolesHasPermissions(userId);
        request.getSession().setAttribute("permissions",list);
        return "main";
    }
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("user/toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }


    @RequestMapping("user/toSettingPage")
    public String setInfo(HttpServletRequest request){
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.setAttribute("user",user);
        return "user/setting";
    }
}
