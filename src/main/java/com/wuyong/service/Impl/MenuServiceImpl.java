package com.wuyong.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wuyong.domain.*;
import com.wuyong.mapper.MenuMapper;
import com.wuyong.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public PageListRes getMenuList(QueryVo vo) {
        Page<Object> page = PageHelper.startPage(vo.getPage(), vo.getRows());
        List<Menu> menus = menuMapper.selectAll();
        /*封装成pageList*/
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(menus);
        return pageListRes;
    }

    @Override
    public List<Menu> parentList() {
        return menuMapper.selectAll();
    }
    @Override
    public AjaxRes saveMenu(Menu menu) {
        AjaxRes ajaxRes = new AjaxRes();
        try {
            System.out.println("保存菜单-----");
            System.out.println(menu);
            menuMapper.insert(menu);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;
    }

    @Override
    public AjaxRes updateMenu(Menu menu) {
        AjaxRes ajaxRes = new AjaxRes();
        /*判断 选择的父菜单  是不是自己的子菜单*/
        Long id=menu.getParent().getId();

        while (id!=null){
            id= menuMapper.selectParentId(id);
            if(id==menu.getId()){
                ajaxRes.setSuccess(false);
                ajaxRes.setMsg("不能设置自己的子菜单为父菜单");
                return ajaxRes;
            }
        }

        /*更新*/
        try {
            System.out.println("更新菜单-----");
            menuMapper.updateByPrimaryKey(menu);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }

    @Override
    public AjaxRes deleteMenu(Long id) {
        AjaxRes ajaxRes = new AjaxRes();
        try {
            /*打破菜单关系*/
            menuMapper.updateMenuRel(id);
            /*删除记录*/
            menuMapper.deleteByPrimaryKey(id);
            ajaxRes.setMsg("删除成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("删除失败");
        }
        return ajaxRes;
    }

    @Override
    public List<Menu> getTreeData() {
        List<Menu> treeData = menuMapper.getTreeData();
        /*判断权限*/

        /*如果没有权限就重集合当中移除*/

        /*获取用户  判断用户是否是管理员  是就不需要进行判断*/
        Subject subject = SecurityUtils.getSubject();
        /*取出当前用户*/
        Employee employee = (Employee) subject.getPrincipal();
        if(!employee.getAdmin()){
            /*权限校验*/
            checkPermission(treeData);
        }
        return treeData;
    }
    public void checkPermission(List<Menu> menus){
        Subject subject = SecurityUtils.getSubject();

        /*遍历所有菜单及子菜单*/
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()){
            Menu menu = iterator.next();
            if(menu.getPermission()!=null){
                /*判断当前用户是否有权限对象   如果说没有 遍历菜单的时候就移除*/
                String presource = menu.getPermission().getPresource();
                if(!subject.isPermitted(presource)){
                    iterator.remove();;
                    continue;
                }
            }
            /*判断是否有子菜单   有的话同样要做权限校验*/
            if(menu.getChildren().size()>0){
                checkPermission(menu.getChildren());
            }
        }
    }
}
