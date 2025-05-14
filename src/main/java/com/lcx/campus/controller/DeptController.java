package com.lcx.campus.controller;


import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IDeptService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 院校表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Resource
    private IDeptService deptService;

    /**
     * 查询部门列表，封装成TreeSelect
     */
    @GetMapping("/treeSelect")
    @PreAuthorize("hasAnyAuthority('system:dept:list')")
    public Result treeSelect() {
        return deptService.treeSelect();
    }

    /**
     * 查询部门列表,树型结构
     */
    @PostMapping("/selectDeptTreeList")
    @PreAuthorize("hasAnyAuthority('system:dept:list')")
    public Result selectDeptTreeList(@RequestBody Dept dept) {
        return deptService.selectDeptTreeList(dept);
    }

    /**
     * 添加新的部门
     */
    @PostMapping("/addDept")
    @PreAuthorize("hasAnyAuthority('system:dept:add')")
    public Result addDept(@Validated({Dept.insert.class}) @RequestBody Dept dept) {
        return deptService.addDept(dept);
    }

    /**
     * 删除单个部门
     */
    @DeleteMapping("/deleteDept/{deptId}")
    @PreAuthorize("hasAnyAuthority('system:dept:remove')")
    public Result deleteDept(@PathVariable Long deptId) {
        return deptService.deleteDept(deptId);
    }

    /**
     * 修改部门
     */
    @PutMapping("/updateDept")
    @PreAuthorize("hasAnyAuthority('system:dept:edit')")
    public Result updateDept(@Validated({Dept.update.class}) @RequestBody Dept dept) {
        return deptService.updateDept(dept);
    }

    /**
     * 仅更改部门状态
     */
    @PutMapping("/updateDeptStatus")
    @PreAuthorize("hasAnyAuthority('system:dept:edit')")
    public Result updateDeptStatus(@RequestParam Long deptId, @RequestParam String status) {
        return deptService.updateDeptStatus(deptId, status);
    }

    /**
     * 查询部门详情
     */
    @GetMapping("/getDeptInfo/{deptId}")
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    public Result getDeptInfo(@PathVariable Long deptId) {
        return deptService.getDeptInfo(deptId);
    }
}
