package com.lcx.campus.service.impl;

import com.lcx.campus.domain.OperateLog;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.mapper.OperateLogMapper;
import com.lcx.campus.service.IOperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements IOperateLogService {

    @Override
    public Result insertOperateLog(OperateLog operateLog) {
        boolean isSuccess = save(operateLog);
        return isSuccess ? Result.success() : Result.fail();
    }
}
