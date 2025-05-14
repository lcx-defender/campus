package com.lcx.campus.service;

import com.lcx.campus.domain.OperateLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.dto.Result;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IOperateLogService extends IService<OperateLog> {

    Result insertOperateLog(OperateLog operLog);

    Result pageList(OperateLog operateLog);

    void clean();
}
