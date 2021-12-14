package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.bean.SaleChance;
import com.yjx.crm.query.CusDevPlanQuery;
import com.yjx.crm.service.CusDevPlanService;
import com.yjx.crm.service.SalChanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Autowired
    private CusDevPlanService cusDevPlanService;

    @Autowired
    private SalChanceService salChanceService;
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    /*进入开发计划项数据页面*/
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Model model,Integer sid){
        //根据ID获取营销机会数据
        SaleChance saleChance = salChanceService.selectByPrimaryKey(sid);
        //将数据添加至model中
        model.addAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanParams(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanParams(cusDevPlanQuery);
    }

}
