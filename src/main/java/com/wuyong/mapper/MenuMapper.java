package com.wuyong.mapper;

import com.wuyong.domain.Menu;
import java.util.List;

public interface MenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    Menu selectByPrimaryKey(Long id);

    List<Menu> selectAll();

    int updateByPrimaryKey(Menu record);

    Long selectParentId(Long id);
    /*更新菜单关系*/
    void updateMenuRel(Long id);

    List<Menu> getTreeData();
}