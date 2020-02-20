package com.wuyong.web.realm;

import com.wuyong.domain.Employee;
import com.wuyong.service.EmployeeService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRealm extends AuthorizingRealm {
    @Autowired
    private EmployeeService employeeService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("来到了认证");
        String username = (String) token.getPrincipal();
        System.out.println(username);
        /*到数据库查询有没有当前用户*/
        Employee employee=employeeService.getEmployeeWithUserName(username);
        System.out.println(employee);
        if(employee==null){
            return null;
        }
        /*认证*/
        /*参数 主题、正确密码、盐、当前realm的名字*/
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(employee,
                employee.getPassword(),
                ByteSource.Util.bytes(employee.getUsername()),
                this.getName());
        return info;
    }

    /*授权*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        System.out.println("来到了授权--------");
        /*获取当前用户的身份信息*/
        Employee employee = (Employee) principal.getPrimaryPrincipal();
        /*根据当前的用户查询角色和权限*/
        List<String> roles=new ArrayList<>();
        List<String> permissions=new ArrayList<>();

        /*判断当前用户是不管理员 如果是管理员就拥有所有权限*/
        if(employee.getAdmin()){
            /*拥有所有权限*/
            permissions.add("*:*");
        }else {
            /*查询角色*/
            roles=employeeService.getRolesById(employee.getId());
            System.out.println("roles=========="+roles);
            /*查权限*/
            permissions=employeeService.getPermissionById(employee.getId());
            System.out.println("permissions========="+permissions);
        }

        /*给授权信息*/
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(permissions);
        return info;
    }


}
