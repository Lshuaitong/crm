package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.Module;
import com.yjx.crm.dto.TreeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
public interface ModuleMapper extends BaseMapper<Module,Integer> {
    int insert(Module record);

    /*查询Id pId name*/
    List<TreeDto> queryAllModules();
}