package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.User;
import com.yjx.crm.exceptions.ParamsException;
import com.yjx.crm.model.UserModel;
import com.yjx.crm.query.UserQuery;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /*
    * 用户分页查询
    * */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryUserParams(UserQuery userQuery){
        return userService.queryUserParams(userQuery);
    }

    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    /*
    * 用户登录
    * */
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.login(userName, userPwd);
        resultInfo.setResult(userModel);
        return resultInfo;
    }
    /*修改密码*/
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest request, String oldPwd,String newPwd, String confirmPwd){
        ResultInfo resultInfo = new ResultInfo();
        //获取userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPassword(userId,oldPwd,newPwd,confirmPwd);
        return resultInfo;
    }

    /*修改信息*/
    @PostMapping("setInfo")
    @ResponseBody
    public ResultInfo setInfo(User user){
        ResultInfo resultInfo = new ResultInfo();
        userService.updateByPrimaryKeySelective(user);
        return resultInfo;
    }

    /*获取销售人员*/
    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSal(){
        return userService.querySal();
    }

    /*添加用户*/
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.saveUser(user);
        return success("添加用户成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("更新用户成功");
    }
    @RequestMapping("addOrUpdateUserPage")
    public String saveOrUpdateUser(Integer id, Model model){

        if (id!=null){
            User user = userService.selectByPrimaryKey(id);
            System.out.println(user);
            model.addAttribute("user",user);

        }
        return "user/add_update";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success("删除用户成功");
    }
}
