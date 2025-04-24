package com.lcx.campus.service;

import com.lcx.campus.domain.DormitoryInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.dto.Result;

import java.util.List;

/**
 * <p>
 * 学生宿舍信息表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IDormitoryInfoService extends IService<DormitoryInfo> {

    /**
     * 学生个人宿舍信息
     * @param studentId
     * @return
     */
    Result getStudentDormitoryInfo(String studentId);

    /**
     * 根据用户ID查询宿舍信息
     * @param userId
     * @return
     */
    Result getSelfDormitoryInfo(Long userId);

    /**
     * 分页查询宿舍信息
     */
    Result pageList(DormitoryInfo dormitoryInfo);

    /**
     * 添加宿舍信息
     */
    Result add(DormitoryInfo dormitoryInfo);

    /**
     * 修改宿舍信息
     * @param dormitoryInfo
     * @return
     */
    Result edit(DormitoryInfo dormitoryInfo);

    Result clear(Long id);

    Result clearBatch(List<Long> ids);
}
