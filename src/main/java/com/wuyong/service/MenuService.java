package com.wuyong.service;

import com.wuyong.domain.AjaxRes;
import com.wuyong.domain.Menu;
import com.wuyong.domain.PageListRes;
import com.wuyong.domain.QueryVo;

import java.util.List;

public interface MenuService {

    PageListRes getMenuList(QueryVo vo);

    List<Menu> parentList();

    AjaxRes saveMenu(Menu menu);

    AjaxRes updateMenu(Menu menu);

    AjaxRes deleteMenu(Long id);

    List<Menu> getTreeData();
}
