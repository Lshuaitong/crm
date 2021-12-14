package com.yjx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.CusDevPlan;
import com.yjx.crm.mapper.CusDevPlanMapper;
import com.yjx.crm.query.CusDevPlanQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Autowired
    private CusDevPlanMapper cusDevPlanMapper;

    /*
    * 多条件查询计划列表
    * */
    public Map<String,Object> queryCusDevPlanParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();
        //分页
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<CusDevPlan>(selectByParams(cusDevPlanQuery));
        //设置map
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }
}
