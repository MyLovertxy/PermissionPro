package com.wuyong.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuyong.domain.AjaxRes;
import com.wuyong.domain.PageListRes;
import com.wuyong.domain.QueryVo;
import com.wuyong.domain.Role;
import com.wuyong.service.RoleService;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class RoleController {
    /*注入业务层*/
    @Autowired
    private RoleService roleService;
    @RequestMapping("/role")
    public String menu(){
        return "role";
    }
    /*接收 请求角色列表*/
    @RequestMapping("/getRoles")
    @ResponseBody
    public PageListRes getRoles(QueryVo vo){
        System.out.println(vo);
        /*调用业务层，查询角色列表*/
        PageListRes roles = roleService.getRoles(vo);
        return roles;
    }

    @RequestMapping("saveRole")
    @ResponseBody
    @RequiresPermissions("role:add")
    public AjaxRes saveRole(Role role){
        return  roleService.saveRole(role);
    }

    /*更新角色*/
    @RequestMapping("updateRole")
    @ResponseBody
    @RequiresPermissions("role:edit")
    public AjaxRes updateRole(Role role){
        System.out.println("更新角色------");
        System.out.println(role);
        roleService.updateRole(role);
        AjaxRes ajaxRes = new AjaxRes();
        try {
            roleService.updateRole(role);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }
    /*接收删除请求*/
    @RequestMapping("deleteRole")
    @ResponseBody
    @RequiresPermissions("role:delete")
    /*删除角色*/
    public AjaxRes deleteRole(Long rid){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            roleService.deleteRole(rid);
            ajaxRes.setMsg("删除角色成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("删除角色失败");
        }
        return ajaxRes;
    }
    @RequestMapping("roleList")
    @ResponseBody
    public List<Role> roleList(){
       return roleService.roleList();
    }
    /*根据员工id查询对应的角色*/
    @RequestMapping("getRoleByEid")
    @ResponseBody
    public List<Long> getRoleByEid(Long eid){
        return roleService.getRoleByEid(eid);
    }

    @ExceptionHandler(AuthorizationException.class)
    public void handleShiroException(HandlerMethod method, HttpServletResponse response) throws IOException {/*发生异常的方法*/
        /*跳转到一个界面  界面提示没有权限*/
        /*判断当前这个请求是不是json请求 如果是  返回json给浏览器让他自己跳转*/
        /*获取方法上的注解*/
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        if(responseBody!=null){
            //ajax
            AjaxRes ajaxRes = new AjaxRes();
            ajaxRes.setMsg("你没有权限操作");
            ajaxRes.setSuccess(false);
            String s = new ObjectMapper().writeValueAsString(ajaxRes);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(s);
        }else {
            response.sendRedirect("/nopermission.jsp");
        }
    }
}
