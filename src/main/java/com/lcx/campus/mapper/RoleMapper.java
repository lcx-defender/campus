package com.lcx.campus.mapper;

import com.lcx.campus.domain.Role;
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

    List<Role> selectRoleByUserId(Long userId);
}
