package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User,Integer> {

    User selectByUserName(String userName);
    List<Map<String,Object>> querySales();

}