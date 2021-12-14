package com.yjx.crm.controller;

import com.yjx.crm.annotation.RequirePermission;
import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.Role;
import com.yjx.crm.query.RoleQuery;
import com.yjx.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        List<Map<String, Object>> maps = roleService.queryAllRoles(userId);
        System.out.println(maps);
        return maps;
    }

    /*返回角色主页面*/
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /*查询角色列表*/
    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "60")
    public Map<String,Object> userList(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    /*添加响应*/
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("添加角色成功");
    }
    /*更新响应*/
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.update(role);
        return success("更新角色成功");
    }
    //前端反映
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRole(Integer id, Model model){
        if (id!=null){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }
    //删除
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("删除角色成功");
    }

    //授权管理
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }
    //添加权限
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return success("权限添加成功");
    }
}
