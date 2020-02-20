package com.wuyong.service;

import com.wuyong.domain.AjaxRes;
import com.wuyong.domain.Employee;
import com.wuyong.domain.PageListRes;
import com.wuyong.domain.QueryVo;

import java.util.List;

public interface EmployeeService {
    public PageListRes getEmployee(QueryVo vo);
    /*保存员工*/
    void saveEmployee(Employee employee);
    /*更新员工*/
    void updateEmployee(Employee employee);
    /*设置员工的离职状态*/
    AjaxRes updateState(long id);
    /*到数据库查询有没有当前用户*/
    Employee getEmployeeWithUserName(String username);
    /*根据用户的id来查询角色编号的名称*/
    List<String> getRolesById(Long id);
    /*根据用户的id查询权限 资源名称*/
    List<String> getPermissionById(Long id);
}
