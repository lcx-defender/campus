package com.lcx.campus.controller;


import com.lcx.campus.domain.DictData;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IDictDataService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/dict-data")
public class DictDataController {

    @Resource
    private IDictDataService dictDataService;

    /**
     * 分页查找数据字典
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyAuthority('system:dict:list')")
    public Result pageList(@RequestBody DictData dictData) {
        return dictDataService.pageList(dictData);
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping("/getInfo/{dictCode}")
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    public Result getInfo(@PathVariable Long dictCode) {
        return dictDataService.getInfo(dictCode);
    }

    /**
     * 根据dictType查询相关dictData
     */
    @GetMapping("/type/{dictType}")
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    public Result dictType(@PathVariable String dictType) {
        return dictDataService.dictType(dictType);
    }

    /**
     * 新增字典数据
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('system:dict:add')")
    public Result add(@Validated @RequestBody DictData dictData) {
        return dictDataService.add(dictData);
    }

    /**
     * 修改字典数据
     */
    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('system:dict:edit')")
    public Result update(@Validated @RequestBody DictData dictData) {
        return dictDataService.updateDict(dictData);
    }

    /**
     * 删除字典数据
     */
    @DeleteMapping("/delete/{dictCodes}")
    @PreAuthorize("hasAnyAuthority('system:dict:remove')")
    public Result deleteDictDataByIds(@PathVariable Long[] dictCodes) {
        return dictDataService.deleteDictDataByIds(dictCodes);
    }
}
