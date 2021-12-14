package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    int insert(UserRole record);

    Integer countUserRoleByUserId(Integer userId);


    int deleteByUserID(Integer userId);
}