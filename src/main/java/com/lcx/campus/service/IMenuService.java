package com.lcx.campus.service;

import com.lcx.campus.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);
}
