package com.lcx.campus.service.impl;

import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.User;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.DeptMapper;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.mapper.TeacherMapper;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 院校表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private StudentMapper studentMapper;

    @Resource

    /**
     * 根据用户id查询用户所在部门
     *
     * @param userId
     * @return 部门信息
     */
    @Override
    public Long getDeptIdByUserId(Long userId) {
        User user = userMapper.selectById(userId);
        Integer userType = user.getUserType();
        if (userType.equals(UserType.TEACHER.ordinal())) {
            return teacherMapper.selectDeptByUserId(userId);
        } else if(userType.equals(UserType.STUDENT.ordinal())) {
            return studentMapper.selectClassByUserId(userId);
        } else {
            return null;
        }
    }

    /**
     * 判断第一个参数是否是第二个参数的父部门
     *
     * @param deptId1 第一个部门id
     * @param deptId2 第二个部门id
     * @return 部门信息
     */
    public boolean isParentDept(Long deptId1, Long deptId2) {
        Dept dept = getById(deptId2);
        if (dept == null) {
            return false;
        }
        if (dept.getParentId() == null || dept.getParentId() == 0) {
            return false;
        }
        if (dept.getParentId().equals(deptId1)) {
            return true;
        }
        return isParentDept(deptId1, dept.getParentId());
    }

    @Override
    public Long getSelfDeptId() {
        return getDeptIdByUserId(SecurityUtils.getUserId());
    }
}
