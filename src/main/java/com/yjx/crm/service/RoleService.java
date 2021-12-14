package com.yjx.crm.service;

import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.Permission;
import com.yjx.crm.bean.Role;
import com.yjx.crm.mapper.ModuleMapper;
import com.yjx.crm.mapper.PermissionMapper;
import com.yjx.crm.mapper.RoleMapper;
import com.yjx.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private ModuleMapper moduleMapper;

    /*查询所有角色*/
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /*添加角色*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role){
        //角色名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        //判断角色名是否存在
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null,"该角色已存在");
        //设置角色信息
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //验证是否添加成功
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"添加角色失败");
    }
    /*修改角色*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Role role){
        //判断要修改的记录是否存在
        AssertUtil.isTrue((role.getId() == null || roleMapper.selectByPrimaryKey(role.getId())==null),"待更新记录不存在");
        //验证更改后的角色是否存在
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp!= null,"该角色已存在");
        //设置时间
        role.setUpdateDate(new Date());
        //判断是否更新成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"更新角色失败");
    }

    /*删除角色*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        //验证待删记录是否存在
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue((role == null || roleId ==null),"待删记录不存在");
        //设置
        role.setIsValid(0);
        //验证是否删除成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除角色失败");
    }

    /*用户授权*/
    public void addGrant(Integer[] mids,Integer roleId){
        //通过Id获取是否存在授权角色
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue((role == null || roleId == null),"待授权角色不存在");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0){
            AssertUtil.isTrue(permissionMapper.deleteByPrimaryKey(roleId)<count,"授权角色分配失败");
        }
        if (mids.length>0 && mids!=null){
            List<Permission> listPermission = new ArrayList<>();
            for (Integer mid : mids) {
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                listPermission.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(listPermission)<1,"角色授权添加失败");
        }


    }

}
