package com.lcx.campus.service;

import com.lcx.campus.domain.po.Dept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.po.Student;
import com.lcx.campus.domain.vo.Result;
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

    /**
     * 根据用户id查询所属学校
     */
    Long getUniversityIdByUserId(Long userId);

    boolean isParentDept(Long deptId1, Long deptId2);

    /**
     * 查询自己所在部门ID
     */
    Long getSelfDeptId();

    /**
     * 查询部门列表，封装成TreeSelect
     */
    Result treeSelect();

    /**
     * 查询部门列表，以树型结构返回
     */
    List<TreeSelect> buildDeptTreeSelect(List<Dept> depts);

    /**
     * 构建部门树型结构
     */
    List<Dept> buildDeptTree(List<Dept> depts);

    /**
     * 根据部门ID查询部门树信息
     * @param deptId
     * @return
     */
    Result getDeptInfo(Long deptId);

    /**
     * 查询部门列表，以树型结构返回
     */
    Result selectDeptTreeList(Dept dept);

    Result addDept(Dept dept);

    Result updateDept(Dept dept);

    /**
     * 递归更新子部门
     */
    boolean updateDeptChildren(Dept dept);

    /**
     * 判断当前部门的父部门是否存在
     */
    boolean isParentDeptExist(Long deptId);


    Result deleteDept(Long deptId);
    /**
     * 判断当前部门是否存在未被删除的子部门
     */
    boolean hasChildByDeptId(Long deptId);

    Result updateDeptStatus(Long deptId, String status);

    /**
     * 校验学生的单位信息是否合规
     */
    boolean validateDept(Student student);
}
