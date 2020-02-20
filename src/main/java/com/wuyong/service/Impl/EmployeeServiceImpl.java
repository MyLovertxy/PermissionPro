package com.wuyong.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wuyong.domain.*;
import com.wuyong.mapper.EmployeeMapper;
import com.wuyong.service.EmployeeService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public PageListRes getEmployee(QueryVo vo) {
        System.out.println("getEmployee--------");
        Page<Object> page = PageHelper.startPage(vo.getPage(), vo.getRows());
        List<Employee> employees = employeeMapper.selectAll(vo);
        /*封装成pageList*/
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(employees);
        return pageListRes;
    }
    /*保存员工*/
    @Override
    public void saveEmployee(Employee employee) {
        /*把密码进行加密*/
        Md5Hash md5Hash = new Md5Hash(employee.getPassword(), employee.getUsername(), 2);
        employee.setPassword(md5Hash.toString());
        /*保存员工*/
        employeeMapper.insert(employee);
        /*保存角色  员工关系*/
        for (Role role : employee.getRoles()) {
            employeeMapper.insertEmployeeAndRoleRel(employee.getId(),role.getRid());
        }
    }
    /*更新员工*/
    @Override
    public void updateEmployee(Employee employee) {
        /*打破与角色之间的关系*/
        employeeMapper.deleteRoleRel(employee.getId());
        /*更新员工*/
        employeeMapper.updateByPrimaryKey(employee);
        /*重新建立关系*/
        for (Role role : employee.getRoles()) {
            employeeMapper.insertEmployeeAndRoleRel(employee.getId(),role.getRid());
        }
    }
    /*设置员工的离职状态*/
    @Override
    public AjaxRes updateState(long id) {
        AjaxRes ajaxRes = new AjaxRes();
        try {
            System.out.println("设置员工的离职状态-----");
            System.out.println(id);
            employeeMapper.updateState(id);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }
    /*到数据库查询有没有当前用户*/
    @Override
    public Employee getEmployeeWithUserName(String username) {

        return employeeMapper.getEmployeeWithUserName(username);
    }
    /*根据用户的id来查询角色编号的名称*/
    @Override
    public List<String> getRolesById(Long id) {
        return employeeMapper.getRoleById(id);
    }
    /*根据用户的id查询权限 资源名称*/
    @Override
    public List<String> getPermissionById(Long id) {
        return employeeMapper.getPermissionById(id);
    }
}
