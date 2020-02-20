package com.wuyong.mapper;

import com.wuyong.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long rid);

    int insert(Role record);

    Role selectByPrimaryKey(Long rid);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);
    /*保存角色与权限之间的关系*/
    void insertRoleAndPermissionRel(@Param("rid") Long rid,@Param("pid") Long pid);

    void deletePermissionRel(Long rid);
    /*根据员工id查询对应的角色*/
    List<Long> getRoleByEid(Long eid);
}