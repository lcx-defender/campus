package com.lcx.campus.mapper;

import com.lcx.campus.domain.po.User;
import com.lcx.campus.domain.po.UserRole;

import java.util.List;

/**
 * <p>
 * 用户和角色关联表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface UserRoleMapper {

    /**
     * 通过userId查询所具备的所有角色的roleId
     */
    List<Long> selectRoleIdByUserId(Long userId);

    /**
     * 删除所有跟userId有关的角色
     */
    boolean deleteByUserId(Long userId);

    int insertBatch(Long userId, Long[] roleIds);

    /**
     * 通过roleId查询所具备的所有用户的userId
     */
    List<UserRole> selectByRoleId(Long roleId);

    List<User> selectUserByRoleId(Long roleId);

    boolean deleteByRoleId(Long roleId);

    List<User> selectByRoleIds(Long[] roleIds);
}
