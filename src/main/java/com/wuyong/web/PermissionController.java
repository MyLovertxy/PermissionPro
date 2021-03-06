package com.wuyong.web;

import com.wuyong.domain.Permission;
import com.wuyong.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PermissionController {
    /*注入业务*/
    @Autowired
    private PermissionService permissionService;
    @RequestMapping("permissionList")
    @ResponseBody
    public List<Permission> permissionList(){
        List<Permission> permissions = permissionService.getPermissions();
        return permissions;

    }
    /*根据角色来查询权限*/
    @RequestMapping("getPermissionByRid")
    @ResponseBody
    public List<Permission> getPermissionByRid(Long rid){
        System.out.println("角色rid="+rid);
        List<Permission> list= permissionService.getPermissionByRid(rid);
        System.out.println(list);
        return list;
    }
}
