package com.wuyong.service;

import com.wuyong.domain.Permission;

import java.util.List;

public interface PermissionService {
    public List<Permission> getPermissions();
    /*根据角色来查询权限*/
    List<Permission> getPermissionByRid(Long rid);
}
