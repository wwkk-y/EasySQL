package com.EasySQL.dao;

import lombok.Data;

/**
 * 数据库用户
 */
@Data
public class DbUser {
    private Long uid;
    private String username;
    private String password;
}
