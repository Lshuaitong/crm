package com.yjx.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeDto {
    private Integer id;
    private Integer pId;
    private String name;
    private boolean checked;
}
