package com.lcx.campus.controller;


import com.lcx.campus.domain.DictType;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.service.IDictTypeService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/dict-type")
public class DictTypeController {

    @Resource
    private IDictTypeService dictTypeService;

    /**
     * 分页查询字典类型
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyAuthority('system:dict:list')")
    public Result pageList(@RequestBody DictType dictType) {
        return dictTypeService.pageList(dictType);
    }

    /**
     * 查询字典类型详细
     */
    @GetMapping("/getDictType/{dictId}")
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    public Result getDictType(@PathVariable Long dictId) {
        return Result.success(dictTypeService.getById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/addDictType")
    @PreAuthorize("hasAnyAuthority('system:dict:add')")
    public Result addDictType(@RequestBody DictType dictType) {
        return dictTypeService.addDictType(dictType);
    }

    /**
     * 刷新字典缓存
     */
    @PostMapping("/refreshCache")
    @PreAuthorize("hasAnyAuthority('system:dict:remove')")
    public Result refreshCache() {
        dictTypeService.refreshCache();
        return Result.success("刷新缓存成功");
    }

}
