package com.wuyong.service.Impl;

import com.wuyong.domain.Permission;
import com.wuyong.mapper.PermissionMapper;
import com.wuyong.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public List<Permission> getPermissions() {
        return permissionMapper.selectAll();
    }
    /*根据角色来查询权限*/
    @Override
    public List<Permission> getPermissionByRid(Long rid) {
        return  permissionMapper.selectPermissionByRid(rid);
    }
}
