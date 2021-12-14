package com.yjx.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 用户登录成功后的信息
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String userIdStr;
    private String userName;
    private String trueName;
}
