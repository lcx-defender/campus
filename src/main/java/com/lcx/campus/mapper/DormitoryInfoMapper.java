package com.lcx.campus.mapper;

import com.lcx.campus.domain.po.DormitoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 学生宿舍信息表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface DormitoryInfoMapper extends BaseMapper<DormitoryInfo> {
    /**
     * 查询是否当前床位是否已经有人居住
     */
    DormitoryInfo selectDormitoryInfoByLocation(DormitoryInfo dormitoryInfo);
    /**
     * 查询当前用户的宿舍信息
     */
    DormitoryInfo selectDormitoryInfoByUserId(Long userId);

    @Select("SELECT * FROM dormitory_info WHERE student_id = #{studentId}")
    DormitoryInfo selectDormitoryInfoByStudentId(String studentId);

    List<DormitoryInfo> selectDormitoryInfoList(DormitoryInfo dormitoryInfo, List<String> studentIds);
}