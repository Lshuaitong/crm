package com.yjx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.SaleChance;
import com.yjx.crm.mapper.SaleChanceMapper;
import com.yjx.crm.query.SalChanceQuery;
import com.yjx.crm.utils.AssertUtil;
import com.yjx.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SalChanceService extends BaseService<SaleChance,Integer> {

    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /*
    * 多条件查询营销机会列表
    * */
    public Map<String,Object> querySalChanceParams(SalChanceQuery salChanceQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(salChanceQuery.getPage(),salChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(salChanceQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /*
    *
    * 修改营销数据
    *
    * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //获取id
        SaleChance chance = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断chace是否存在
        AssertUtil.isTrue(chance==null,"待更新用户不存在");
        //验证待更新用户的基本信息
        checkSalChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置修改时间
        saleChance.setUpdateDate(new Date());
        //状态的更新
        //如果原来没有分配人 并且 修改后有分配人   未分配改为已分配
        if (StringUtils.isBlank(saleChance.getAssignMan()) && StringUtils.isNotBlank(chance.getAssignMan())){
            saleChance.setAssignMan("");
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setDevResult(0);
        }
        //相反
        if (StringUtils.isNotBlank(saleChance.getAssignMan()) && StringUtils.isBlank(chance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        }
        //判断是否修改成功
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"更新失败");
    }

    /*
    * 添加营销机会
    * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //验证
        checkSalChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //state是否已分配   0 代表未分配  1代表已分配
        if (StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(1);
        }
        if (StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //如果已分配  创建时间为当前时间
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setIsValid(1);
        //判断添加是否成功
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"添加营销机会失败");
    }

    private void checkSalChanceParams(String customerName, String linkMan, String linkPhone) {
        //客户名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空");
        //联系人不能为空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        //手机号不能为空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"手机号不能为空");
        //手机号格式必须正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确");
    }

    /*删除营销机会*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        //判断ids是否存在
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择需要删除的数据");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<1,"营销机会删除失败");
    }

}
