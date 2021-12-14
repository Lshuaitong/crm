package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.Module;
import com.yjx.crm.bean.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    int insert(Permission record);
    int countPermissionByRoleId(Integer roleId);
    List<Integer> queryRoleHasAllModuleIdsByRoleId(Integer roleId);

    List<String> queryUserHasRolesHasPermissions(Integer userId);

}