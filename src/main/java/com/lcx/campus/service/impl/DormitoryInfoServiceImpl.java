package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.DormitoryInfo;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.enums.DeptLevel;
import com.lcx.campus.mapper.DeptMapper;
import com.lcx.campus.mapper.DormitoryInfoMapper;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IDormitoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生宿舍信息表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class DormitoryInfoServiceImpl extends ServiceImpl<DormitoryInfoMapper, DormitoryInfo> implements IDormitoryInfoService {

    @Resource
    private DormitoryInfoMapper dormitoryInfoMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private DeptMapper deptMapper;

    @Override
    public Result getStudentDormitoryInfo(String studentId) {
        return Result.success(getById(studentId));
    }

    @Override
    public Result getSelfDormitoryInfo(Long userId) {
        // 获取学生ID
        Student student = studentMapper.selectById(userId);
        if(student == null) {
            return Result.fail("未找到当前用户学生信息");
        }
        String studentId = student.getStudentId();
        if(StringUtils.isEmpty(studentId)) {
            return Result.fail("未找到当前用户学生信息");
        }
        return getStudentDormitoryInfo(studentId);
    }

    /**
     * 分页查询宿舍信息
     */
    @Override
    public Result pageList(DormitoryInfo dormitoryInfo) {
        Page<DormitoryInfo> queryPage = dormitoryInfo.toMpPage();
        // 设置查询条件
        List<String> studentIds = null;
        if(dormitoryInfo.getDeptId() != null) {
            // 根据部门ID查询学生ID
            Long deptId= dormitoryInfo.getDeptId();
            Dept dept = deptMapper.selectById(deptId);
            if(dept == null) {
                return Result.fail("未找到部门信息");
            }
            if(dept.getLevel() == DeptLevel.UNIVERSITY.getLevel()) {
                List<Student> students = studentMapper.selectStudentListByUniversityId(deptId);
                // 过滤只留下在读学生ID
                studentIds = students.stream()
                        .map(Student::getStudentId)
                        .filter(studentId -> studentId != null)
                        .collect(Collectors.toList());
            } else if(dept.getLevel() == DeptLevel.INSTITUTE.getLevel()) {
                List<Student> students = studentMapper.selectStudentListByInstituteId(deptId);
                // 过滤只留下在读学生ID
                studentIds = students.stream()
                        .map(Student::getStudentId)
                        .filter(studentId -> studentId != null)
                        .collect(Collectors.toList());
            } else if(dept.getLevel() == DeptLevel.MAJOR.getLevel()) {
                List<Student> students = studentMapper.selectStudentListByMajorId(deptId);
                // 过滤只留下在读学生ID
                studentIds = students.stream()
                        .map(Student::getStudentId)
                        .filter(studentId -> studentId != null)
                        .collect(Collectors.toList());
            } else if(dept.getLevel() == DeptLevel.CLAZZ.getLevel()) {
                List<Student> students = studentMapper.selectStudentListByClassId(deptId);
                // 过滤只留下在读学生ID
                studentIds = students.stream()
                        .map(Student::getStudentId)
                        .filter(studentId -> studentId != null)
                        .collect(Collectors.toList());
            }
        }
        Page<DormitoryInfo> page = lambdaQuery()
                .eq(dormitoryInfo.getDormitoryId() != null, DormitoryInfo::getDormitoryId, dormitoryInfo.getDormitoryId())
                .eq(dormitoryInfo.getRoomId() != null, DormitoryInfo::getRoomId, dormitoryInfo.getRoomId())
                .eq(dormitoryInfo.getBedId() != null, DormitoryInfo::getBedId, dormitoryInfo.getBedId())
                .in(studentIds != null && !studentIds.isEmpty(), DormitoryInfo::getStudentId, studentIds)
                .page(queryPage);
        PageVo<DormitoryInfo> res = PageVo.of(page);
        return Result.success(res);
    }

    @Override
    public Result add(DormitoryInfo dormitoryInfo) {
        // 检查是否已经存在当前宿舍楼、宿舍号、床位号相同的宿舍信息；若存在，更新为当前新增信息
        DormitoryInfo existingDormitoryInfo = dormitoryInfoMapper.selectOne(
                lambdaQuery()
                        .eq(DormitoryInfo::getDormitoryId, dormitoryInfo.getDormitoryId())
                        .eq(DormitoryInfo::getRoomId, dormitoryInfo.getRoomId())
                        .eq(DormitoryInfo::getBedId, dormitoryInfo.getBedId())
        );
        if (existingDormitoryInfo != null) {
            // 更新宿舍信息
            existingDormitoryInfo.setStudentId(dormitoryInfo.getStudentId());
            dormitoryInfoMapper.updateById(existingDormitoryInfo);
            return Result.success("更新宿舍信息成功");
        } else {
            // 插入新的宿舍信息
            dormitoryInfoMapper.insert(dormitoryInfo);
            return Result.success("添加宿舍信息成功");
        }
    }

    /**
     * 修改宿舍信息
     * @param dormitoryInfo
     * @return
     */
    @Override
    public Result edit(DormitoryInfo dormitoryInfo) {
        return updateById(dormitoryInfo) ? Result.success("修改宿舍信息成功") : Result.fail("修改宿舍信息失败");
    }
}
