package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.OperateLog;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.mapper.OperateLogMapper;
import com.lcx.campus.service.IOperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
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

    @Resource
    private OperateLogMapper operateLogMapper;

    @Override
    public Result insertOperateLog(OperateLog operateLog) {
        boolean isSuccess = save(operateLog);
        return isSuccess ? Result.success() : Result.fail();
    }

    @Override
    public Result pageList(OperateLog operateLog) {
        Page<OperateLog> queryPage = operateLog.toMpPage();
        Page<OperateLog> resPage = lambdaQuery()
                .eq(operateLog.getTitle() != null, OperateLog::getTitle, operateLog.getTitle())
                .eq(operateLog.getBusinessType() != null, OperateLog::getBusinessType, operateLog.getBusinessType())
                .eq(operateLog.getOperatorType() != null, OperateLog::getOperatorType, operateLog.getOperatorType())
                .eq(operateLog.getRequestMethod() != null, OperateLog::getRequestMethod, operateLog.getRequestMethod())
                .eq(operateLog.getUserId() != null, OperateLog::getUserId, operateLog.getUserId())
                .eq(operateLog.getOperateUrl() != null, OperateLog::getOperateUrl, operateLog.getOperateUrl())
                .page(queryPage);
        PageVo<OperateLog> res = PageVo.of(resPage);
        return Result.success(res);
    }

    @Override
    public boolean clean() {
        return operateLogMapper.clearAll();
    }
}
