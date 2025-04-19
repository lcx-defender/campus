package com.lcx.campus.controller;


import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IDeptService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
     * 查询部门列表，以树型结构返回
     */
    @GetMapping("/treeSelect")
    public Result treeSelect() {
        return deptService.treeSelect();
    }
}
