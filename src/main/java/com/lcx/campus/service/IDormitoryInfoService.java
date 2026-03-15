package com.lcx.campus.service;

import com.lcx.campus.domain.po.DormitoryInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.vo.Result;

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
     * 获取宿舍信息列表
     */
    Result getDormitoryInfoPage(DormitoryInfo dormitoryInfo);
    /**
     * 单独增加宿舍信息
     */
    Result addDormitoryInfo(DormitoryInfo dormitoryInfo);

    void addBatchDormitoryInfo(List<DormitoryInfo> list);
    /**
     * 获取当前用户的宿舍信息
     */
    Result getCurrentUserDormitoryInfo();
    /**
     * 修改宿舍信息
     */
    Result update(DormitoryInfo dormitoryInfo);

    /**
     * 条件查询宿舍信息
     */
    List<DormitoryInfo> selectDormitoryInfoList(DormitoryInfo dormitoryInfo);
}
