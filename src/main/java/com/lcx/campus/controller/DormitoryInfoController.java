package com.lcx.campus.controller;


import com.lcx.campus.domain.DormitoryInfo;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IDormitoryInfoService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 学生宿舍信息表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/dormitory-info")
public class DormitoryInfoController {

    @Resource
    private IDormitoryInfoService dormitoryInfoService;

    /**
     * 学生个人宿舍信息
     */
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyAuthority('system:dormitory:query')")
    public Result getStudentDormitoryInfo(@PathVariable String studentId) {
        return dormitoryInfoService.getStudentDormitoryInfo(studentId);
    }

    /**
     * 获取当前登录用户的宿舍信息
     */
    @GetMapping("/getSelfDormitoryInfo")
    public Result getSelfDormitoryInfo() {
        Long userId = SecurityUtils.getUserId();
        return dormitoryInfoService.getSelfDormitoryInfo(userId);
    }

    /**
     * 分页查询宿舍信息
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyAuthority('system:dormitory:list')")
    public Result pageList(@RequestBody DormitoryInfo dormitoryInfo) {
        return dormitoryInfoService.pageList(dormitoryInfo);
    }

    /**
     * 添加宿舍信息
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:dormitory:add')")
    public Result add(@Validated({DormitoryInfo.insert.class}) @RequestBody DormitoryInfo dormitoryInfo) {
        return dormitoryInfoService.add(dormitoryInfo);
    }

    /**
     * 修改宿舍信息
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:dormitory:edit')")
    public Result edit(@Validated @RequestBody DormitoryInfo dormitoryInfo) {
        return dormitoryInfoService.edit(dormitoryInfo);
    }
}
