package com.lcx.campus.mapper;

import com.lcx.campus.domain.po.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据userId查询具有的角色
     */
    List<Role> selectRoleByUserId(Long userId);

    boolean isAdminByUserId(Long userId);
}
