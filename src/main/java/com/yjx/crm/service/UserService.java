package com.yjx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjx.crm.base.BaseService;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.User;
import com.yjx.crm.bean.UserRole;
import com.yjx.crm.mapper.UserMapper;
import com.yjx.crm.mapper.UserRoleMapper;
import com.yjx.crm.model.UserModel;
import com.yjx.crm.query.UserQuery;
import com.yjx.crm.utils.AssertUtil;
import com.yjx.crm.utils.Md5Util;
import com.yjx.crm.utils.PhoneUtil;
import com.yjx.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;
    /*验证用户登录*/
    public UserModel login(String userName, String userPwd){
        //判断用户名密码是否为空
        checkIsBlank(userName,userPwd);
        //用户是否存在
        User user = userMapper.selectByUserName(userName);
        AssertUtil.isTrue(user==null,"用户不存在");
        //验证密码是否一致
        checkPwd(user.getUserPwd(),userPwd);
        //验证成功后 返回用户信息
        return buildUserInfo(user);

    }

    private void checkIsBlank(String userName, String userPwd) {
        //用户名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

    /*用户登陆成功后信息*/
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }



    /*检查密码是否一致*/
    private void checkPwd(String userPwd, String userPwd1) {
        userPwd1 = Md5Util.encode(userPwd1);
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"密码不正确");
    }

    /*修改密码*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPwd,String newPwd,String confirmPwd){
        //判断用户是否存在
        User user = userMapper.selectByPrimaryKey(userId);
        //密码验证
        checkPasswordParams(user,oldPwd,newPwd,confirmPwd);

        //设置用户密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //执行更新
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败");
    }

    private void checkPasswordParams(User user, String oldPwd, String newPwd, String confirmPwd) {
        //uer对象  非空验证
        AssertUtil.isTrue(user==null,"用户未登录或不存在");
        //原密码不能为空
        System.out.println(oldPwd);
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原密码不能为空");
        //原密码必须正确
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPwd))),"原密码错误");
        //新密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        //新密码不能和原密码一致
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原密码一致");
        //确认密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空");
        //确认密码必须和新密码一致
        AssertUtil.isTrue(!(newPwd.equals(confirmPwd)),"新密码与确认密码不一致");

    }

    /*查找销售人员*/
    public List<Map<String,Object>> querySal(){
        return userMapper.querySales();
    }

    /*
    * 多条件分页查询
    * */
    public Map<String,Object> queryUserParams(UserQuery userQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(userQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /*用户添加操作*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user){
        //验证参数是否为空
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        //设置user参数
        user.setIsValid(1);
        //设置创建时间
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置密码
        user.setUserPwd(Md5Util.encode("123456"));
        //判断是否添加成功
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加用户失败");
        //添加角色
        Integer id = userMapper.selectByUserName(user.getUserName()).getId();
        relaionUserRole(id, user.getRoleIds());
    }
    /*更新用户操作*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //根据Id查找待更新用户
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(user1 == null,"待更新记录不存在");
        //验证参数
//        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        Integer id = userMapper.selectByUserName(user.getUserName()).getId();
        relaionUserRole(id,user.getRoleIds());
        //设置修改时间
        user.setUpdateDate(new Date());
        //检查是否修改成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"更新用户信息失败");

    }
    private void relaionUserRole(Integer userid, String roleIds) {
        int count = userRoleMapper.countUserRoleByUserId(userid);
        if (count > 0){
            AssertUtil.isTrue(userRoleMapper.deleteByUserID(userid)!= count,"用户角色分配失败");
        }
        if (StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoles = new ArrayList<>();
            for (String s: roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userid);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles)<userRoles.size(),"用户角色分配失败");
        }


    }

    /*校验添加信息的参数*/
    private void checkParams(String userName, String email, String phone) {
        //用户名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        User user = userMapper.selectByUserName(userName);
        AssertUtil.isTrue(user!=null,"用户名已存在");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }
    /*删除用户信息*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids){
        AssertUtil.isTrue((ids==null || ids.length==0),"请选择需要删除的用户");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<1,"删除用户失败");
    }


}
