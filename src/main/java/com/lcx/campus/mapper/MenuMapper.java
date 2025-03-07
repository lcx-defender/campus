package com.lcx.campus.mapper;

import com.lcx.campus.domain.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

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

    List<String> selectMenuPermsByUserId(Long userId);

    @Select("select distinct perms from sys_menu")
    List<String> selectAllMenuPerms();
}
