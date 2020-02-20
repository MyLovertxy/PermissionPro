package com.wuyong.mapper;

import com.wuyong.domain.Employee;
import com.wuyong.domain.QueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    List<Employee> selectAll(QueryVo vo);

    int updateByPrimaryKey(Employee record);
    /*设置员工离职状态*/
    void updateState(long id);
    /*保存角色  员工关系*/
    void insertEmployeeAndRoleRel(@Param("id") Long id, @Param("rid") Long rid);
    /*打破与角色之间的关系*/
    void deleteRoleRel(Long id);
    /*到数据库查询有没有当前用户*/
    Employee getEmployeeWithUserName(String username);
    /*根据用户的id来查询角色编号的名称*/
    List<String> getRoleById(Long id);
    /*根据用户的id查询权限 资源名称*/
    List<String> getPermissionById(Long id);
}