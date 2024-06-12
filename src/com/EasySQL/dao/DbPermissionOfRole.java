package com.EasySQL.dao;

import lombok.Data;

/**
 * 角色的权限
 */
@Data
public class DbPermissionOfRole {
    private Long permissionId;
    private Long roleId;
}
