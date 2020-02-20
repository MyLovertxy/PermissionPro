package com.wuyong.service;

import com.wuyong.domain.AjaxRes;
import com.wuyong.domain.PageListRes;
import com.wuyong.domain.QueryVo;
import com.wuyong.domain.Role;

import java.util.List;

public interface RoleService {
    /*查询角色列表*/
    public PageListRes getRoles(QueryVo vo);
    /*保存角色和权限*/
    AjaxRes saveRole(Role role);
    /*更新角色*/
    void updateRole(Role role);
    /*删除角色*/
    void deleteRole(Long rid);
    /*查询角色列表 无参*/
    List<Role> roleList();
    /*根据员工id查询对应的角色*/
    List<Long> getRoleByEid(Long eid);
}
