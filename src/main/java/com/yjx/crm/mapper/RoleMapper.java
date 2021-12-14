package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.Role;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper extends BaseMapper<Role,Integer> {
   List<Map<String,Object>> queryAllRoles(Integer userId);
   Role queryRoleByRoleName(String roleName);
}