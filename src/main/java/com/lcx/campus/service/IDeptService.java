package com.lcx.campus.service;

import com.lcx.campus.domain.Dept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.TreeSelect;

import java.util.List;

/**
 * <p>
 * 院校表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 根据用户id查询用户所在部门
     */
    Long getDeptIdByUserId(Long userId);

    boolean isParentDept(Long deptId1, Long deptId2);

    /**
     * 查询自己所在部门ID
     */
    Long getSelfDeptId();

    Result treeSelect();

    List<TreeSelect> buildDeptTreeSelect(List<Dept> depts);

    List<Dept> buildDeptTree(List<Dept> depts);
}
