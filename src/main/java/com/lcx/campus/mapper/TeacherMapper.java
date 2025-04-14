package com.lcx.campus.mapper;

import com.lcx.campus.domain.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 教师表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

    Long selectDeptByUserId(Long userId);
}
