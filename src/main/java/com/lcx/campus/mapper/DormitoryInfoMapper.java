package com.lcx.campus.mapper;

import com.lcx.campus.domain.DormitoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

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

    @Update("UPDATE dormitory_info SET student_id = NULL WHERE id = #{id}")
    boolean clearStudent(Long id);

    @Update("UPDATE dormitory_info SET student_id = NULL WHERE id IN (${ids})")
    boolean clearBatch(List<Long> ids);
}
