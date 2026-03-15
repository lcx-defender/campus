package com.lcx.campus.mapper;

import com.lcx.campus.domain.po.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> selectMenuList(Menu menu);

    List<Menu> selectMenuListByUserId(@Param("menu") Menu menu, @Param("userId") Long userId);

    List<String> selectMenuPermsByRoleId(Long roleId);

    List<Menu> selectMenuTreeAll();

    List<Long> selectMenuIdsByRoleId(Long roleId);

    int hasChildByMenuId(Long menuId);

    Menu checkMenuNameUnique(String menuName, Long parentId);

    List<Menu> selectAllMenuListByRoleId(Long roleId);

    List<Menu> selectMenusByRoleId(Long roleId);
}
