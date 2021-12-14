package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.SaleChance;
import com.yjx.crm.query.SalChanceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {

}