package com.EasySQL.dao;

import lombok.Data;

/**
 * 数据库权限
 */
@Data
public class DbPermission {
    private Long id;
    private String name;
    private String remark;
}
