package com.lcx.campus.service;

import com.lcx.campus.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IRoleService extends IService<Role> {

    List<Role> selectRoleByUserId(Long userId);

    boolean isAdmin(Long userId);

    Result getCurrentRole();

    Result getPageList(PageQuery pageQuery, Role role);
}
