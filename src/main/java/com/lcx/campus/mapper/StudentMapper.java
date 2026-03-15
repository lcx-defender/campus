package com.lcx.campus.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.po.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcx.campus.domain.dto.StudentUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 学生表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 根据用户id查询学生班级id
     */
    Long selectClassByUserId(Long userId);

    /**
     * 根据大学id查询学生列表
     */
    @Select("SELECT * FROM student WHERE university_id = #{universityId}")
    List<Student> selectStudentListByUniversityId(Long universityId);
    /**
     * 根据学院id查询学生列表
     */
    @Select("SELECT * FROM student WHERE institute_id = #{instituteId}")
    List<Student> selectStudentListByInstituteId(Long instituteId);
    /**
     * 根据专业id查询学生列表
     */
    @Select("SELECT * FROM student WHERE major_id = #{majorId}")
    List<Student> selectStudentListByMajorId(Long majorId);
    /**
     * 根据班级id查询学生列表
     */
    @Select("SELECT * FROM student WHERE class_id = #{classId}")
    List<Student> selectStudentListByClassId(Long classId);
    /**
     * 根据学生条件查询学生信息列表
     */
    List<Student> selectStudentList(Student student);

    /**
     * 根据学生用户条件查询学生用户列表
     */
    List<StudentUser> selectStudentUserList(StudentUser studentUser);

    //    List<StudentUser> selectStudentUserPage(Page<StudentUser> queryPage, StudentUser studentUser);
    Page<StudentUser> selectStudentUserPage(Page<StudentUser> queryPage, StudentUser studentUser);

    Student selectStudent(Student student);
}
