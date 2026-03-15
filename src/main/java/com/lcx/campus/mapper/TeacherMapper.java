package com.lcx.campus.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.po.Dept;
import com.lcx.campus.domain.po.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcx.campus.domain.dto.TeacherUser;

import java.util.List;

/**
 * <p>
 * 教师表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

    /**
     * 根据用户id查询教师所在部门
     */
    Long selectDeptIdByUserId(Long userId);

    /**
     * 根据用户ID查询教师所在部门详细
     */
    Dept selectDeptByUserId(Long userId);

    List<TeacherUser> pageListTeacherUser(Page<TeacherUser> queryPage, TeacherUser teacherUser);
}
