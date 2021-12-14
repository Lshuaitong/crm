package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.dto.TreeDto;
import com.yjx.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Autowired
    private ModuleService moduleService;

   /* @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(){
        return moduleService.queryAllModules();
    }*/

    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(Integer roleId){
        return moduleService.queryAllModules2(roleId);
    }

}
