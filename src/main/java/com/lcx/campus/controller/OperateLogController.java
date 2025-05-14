package com.lcx.campus.controller;


import com.lcx.campus.domain.OperateLog;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IOperateLogService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 操作日志记录 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/operate-log")
public class OperateLogController {
    @Resource
    private IOperateLogService operateLogService;

    /**
     * 分页查询操作记录
     */
    @PreAuthorize("hasAnyAuthority('monitor:operatelog:list')")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody OperateLog operateLog) {
        return operateLogService.pageList(operateLog);
    }

    /**
     * 删除操作记录
     */
    @PreAuthorize("hasAnyAuthority('monitor:operatelog:remove')")
    @DeleteMapping("/remove/{operateIds}")
    public Result remove(@PathVariable Long[] operateIds) {
        return operateLogService.removeByIds(Arrays.asList(operateIds))? Result.success() : Result.fail("删除失败");
    }

    /**
     * 清空操作记录
     */
    @PreAuthorize("hasAnyAuthority('monitor:operatelog:remove')")
    @DeleteMapping("/removeAll")
    public Result removeAll() {
        operateLogService.clean();
        return Result.success();
    }
}
