package com.lcx.campus.mapper;

import com.lcx.campus.domain.OperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 操作日志记录 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface OperateLogMapper extends BaseMapper<OperateLog> {

    void cleanOperateLog();
}
