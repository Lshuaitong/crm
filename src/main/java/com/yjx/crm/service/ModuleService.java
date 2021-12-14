package com.yjx.crm.service;

import com.yjx.crm.base.BaseService;
import com.yjx.crm.dto.TreeDto;
import com.yjx.crm.mapper.ModuleMapper;
import com.yjx.crm.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    //查询所有菜单
    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }

    //查询某个角色的所有菜单
    public List<TreeDto> queryAllModules2(Integer roleId){
        List<TreeDto> treeDtos = moduleMapper.queryAllModules();
        List<Integer> roleHasModuls = permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        if (roleHasModuls!=null || roleHasModuls.size()>0){
            treeDtos.forEach(treeDto -> {
                if (roleHasModuls.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            });
        }
        return treeDtos;
    }
}
