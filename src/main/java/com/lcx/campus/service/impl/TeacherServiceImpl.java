package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.Teacher;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.TeacherMapper;
import com.lcx.campus.service.IDeptService;
import com.lcx.campus.service.IRoleService;
import com.lcx.campus.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.service.IUserService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 教师表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Resource
    private IUserService userService;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private IRoleService roleService;
    @Resource
    private IDeptService deptService;

    /**
     * 新建教师类型用户
     * @param user
     * @param teacher
     * @return
     */
    @Override
    public Result addTeacher(User user, Teacher teacher) {
        user.setUserType(UserType.TEACHER.getCode());
        // 1. 已经存在当前身份信息的用户就不新建用户
        Long userId = userService.creatUserIfNotExist(user);
        if (userId == null) {
            return Result.fail("添加用户失败");
        }
        // 2. 插入教师信息
        teacher.setUserId(userId); // 将用户信息与教师信息绑定
        teacher.setCreateTime(LocalDateTime.now());
        int insert = teacherMapper.insert(teacher);
        if (insert <= 0) {
            return Result.fail("添加教师信息失败");
        }
        return Result.success("添加教师成功", userId);
    }

    @Override
    public Result pageListTeacher(Teacher teacher) {
        Page<Teacher> queryPage = teacher.toMpPage();
        boolean isAdmin = roleService.isAdmin(SecurityUtils.getUserId());
        if(!isAdmin && teacher.getDeptId() != null && !deptService.isParentDept(deptService.getSelfDeptId(), teacher.getDeptId())) {
            // 非管理员只能查询自己所在部门及子部门的教师
            return Result.fail("没有权限查询该部门的教师信息");
        }
        Page<Teacher> resPage = lambdaQuery()
                .like(teacher.getTeacherName() != null, Teacher::getTeacherName, teacher.getTeacherName())
                .eq(teacher.getDeptId() != null, Teacher::getDeptId, teacher.getDeptId())
                .eq(teacher.getPositionStatus() != null, Teacher::getPositionStatus, teacher.getPositionStatus())
                .eq(teacher.getTitle() != null, Teacher::getTitle, teacher.getTitle())
                .page(queryPage);
        PageVo<Teacher> res = PageVo.of(resPage);
        return Result.success("查询成功", res);
    }

    @Override
    public Result editTeacher(Teacher teacher) {
        teacher.setUpdateTime(LocalDateTime.now());
        return updateById(teacher) ? Result.success("修改成功") : Result.fail("修改失败");
    }
}
