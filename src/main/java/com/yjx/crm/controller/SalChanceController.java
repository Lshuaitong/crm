package com.yjx.crm.controller;

import com.yjx.crm.annotation.RequirePermission;
import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.SaleChance;
import com.yjx.crm.query.SalChanceQuery;
import com.yjx.crm.service.SalChanceService;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SalChanceController extends BaseController {

    @Autowired
    private SalChanceService salChanceService;

    @Autowired
    private UserService userService;

    /*多条件分页查询营销机会*/
    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "101001")
    public Map<String,Object> querySalChanceParams(SalChanceQuery salChanceQuery,Integer flag,HttpServletRequest request){
        if (flag!=null && flag == 1){
            int userId = LoginUserUtil.releaseUserIdFromCookie(request);
            salChanceQuery.setAssignMan(userId);

        }
        return salChanceService.querySalChanceParams(salChanceQuery);
    }

    @RequestMapping("index")
    public String index(){
        return "saleChance/sal_chance";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSalChance(SaleChance saleChance, HttpServletRequest request){
        //获取创建人的ID
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据ID查询创建人的真实姓名
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        //将真是姓名设置给salChance
        saleChance.setCreateMan(trueName);

        salChanceService.addSaleChance(saleChance);

        return success("添加成功");
    }
    //跳转添加页面
    @RequestMapping("addOrUpdateSaleChancePage")
    public String add(){
        System.out.println("------------");
        return "saleChance/add_update";
    }

    //更新营销机会
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        salChanceService.updateSaleChance(saleChance);
        return success("修改营销机会成功");
    }

    //跳转修改营销机会页面
    @RequestMapping("addOrUpdateDialog")
    public String update(Integer id,Model model){
        if (id!=null){
            SaleChance saleChance = salChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        salChanceService.deleteBatch(ids);
        return success("删除营销机会成功");
    }


}
