package com.lcx.campus.service.impl;

import com.lcx.campus.domain.Menu;
import com.lcx.campus.mapper.MenuMapper;
import com.lcx.campus.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> permission = menuMapper.selectMenuPermsByUserId(userId);
        // 将List转为Set
        return Set.copyOf(permission);
    }
}
