package com.EasySQL.dao;

import lombok.Data;

/**
 * 数据库角色
 */
@Data
public class DbRole {
    private Long id;
    private String name;
    private String remark;
}
