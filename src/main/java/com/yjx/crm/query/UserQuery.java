package com.yjx.crm.query;

import com.yjx.crm.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery extends BaseQuery {
    private String userName;
    private String email;
    private String phone;
}
