package com.wuyong.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuyong.domain.AjaxRes;
import com.wuyong.domain.Menu;
import com.wuyong.domain.PageListRes;
import com.wuyong.domain.QueryVo;
import com.wuyong.service.MenuService;
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
public class MenuController {
    @Autowired
    private MenuService menuService;
    @RequestMapping("/menu")
    public String menu(){
        return "menu";
    }

    @RequestMapping("menuList")
    @ResponseBody
    public PageListRes menuList(QueryVo vo){
        PageListRes pageListRes = menuService.getMenuList(vo);
        return pageListRes;
    }
    /*加载父菜单*/
    @RequestMapping("parentList")
    @ResponseBody
    public List<Menu> parentList(){
        return menuService.parentList();
    }


    @RequestMapping("saveMenu")
    @ResponseBody
    @RequiresPermissions("menu:add")
    /*保存菜单*/
    public AjaxRes saveMenu(Menu menu){
        return  menuService.saveMenu(menu);
    }
    @RequiresPermissions("menu:edit")
    /*更新菜单*/
    @RequestMapping("updateMenu")
    @ResponseBody
    public AjaxRes updateMenu(Menu menu){
        return menuService.updateMenu(menu);
    }
    /*删除菜单*/
    @RequestMapping("deleteMenu")
    @ResponseBody
    @RequiresPermissions("menu:delete")
    public AjaxRes deleteMenu(Long id){
        return menuService.deleteMenu(id);
    }


    /*获取树形菜单的数据*/
    @RequestMapping("getTreeData")
    @ResponseBody
    public List<Menu> getTreeData(){
       return menuService.getTreeData();
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
