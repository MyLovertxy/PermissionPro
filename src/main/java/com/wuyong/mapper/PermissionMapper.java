package com.wuyong.mapper;

import com.wuyong.domain.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(Permission record);

    Permission selectByPrimaryKey(Long pid);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);
    /*根据角色来查询权限*/
    List<Permission> selectPermissionByRid( Long rid);
}