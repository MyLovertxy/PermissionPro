package com.wuyong.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wuyong.domain.*;
import com.wuyong.mapper.RoleMapper;
import com.wuyong.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    /*注入角色mapper*/
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public PageListRes getRoles(QueryVo vo) {
        /*调用mapper 查询数据*/
        Page<Object> page = PageHelper.startPage(vo.getPage(), vo.getRows());
        List<Role> roles = roleMapper.selectAll();
        /*封装成pageList*/
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(roles);
        return pageListRes;
    }

    @Override
    public AjaxRes saveRole(Role role) {

        AjaxRes ajaxRes = new AjaxRes();
        try {
            /*保存角色和权限*/
            roleMapper.insert(role);
            /*保存角色与权限之间的关系*/
            for (Permission permission : role.getPermissions()) {
                roleMapper.insertRoleAndPermissionRel(role.getRid(),permission.getPid());
            }

            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;
    }

    @Override
    public void updateRole(Role role) {
        /*打破角色与权限之前的关系*/
        roleMapper.deletePermissionRel(role.getRid());
        /*更新角色*/
        roleMapper.updateByPrimaryKey(role);
        /*重新建立与权限之间的关系*/
        for (Permission permission : role.getPermissions()) {
            roleMapper.insertRoleAndPermissionRel(role.getRid(),permission.getPid());
        }
    }

    @Override
    public void deleteRole(Long rid) {
        /*删除关联的权限*/
        roleMapper.deletePermissionRel(rid);
        /*删除对应的角色*/
        roleMapper.deleteByPrimaryKey(rid);
    }

    @Override
    public List<Role> roleList() {
        return roleMapper.selectAll();
    }
    /*根据员工id查询对应的角色*/
    @Override
    public List<Long> getRoleByEid(Long eid) {
        return roleMapper.getRoleByEid(eid);
    }
}
