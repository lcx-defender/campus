package com.lcx.campus.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.DormitoryInfo;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.listener.ExcelListener;
import com.lcx.campus.service.IDormitoryInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 获取宿舍信息列表
     */
    @PreAuthorize("hasAnyAuthority('campus:dormitory:list')")
    @PostMapping("/getDormitoryInfoPage")
    @Log(title = "获取宿舍信息列表", businessType = BusinessType.QUERY)
    public Result getDormitoryInfoPage(@RequestBody DormitoryInfo dormitoryInfo) {
        return dormitoryInfoService.getDormitoryInfoPage(dormitoryInfo);
    }
    /**
     * 单独增加宿舍信息
     */
    @PreAuthorize("hasAnyAuthority('campus:dormitory:add')")
    @PostMapping("/addDormitoryInfo")
    @Log(title = "单独增加宿舍信息", businessType = BusinessType.INSERT)
    public Result addDormitoryInfo(@Validated(DormitoryInfo.insert.class) @RequestBody DormitoryInfo dormitoryInfo) {
        return dormitoryInfoService.addDormitoryInfo(dormitoryInfo);
    }
    /**
     * excel批量增加宿舍信息
     */
    @PreAuthorize("hasAnyAuthority('campus:dormitory:add')")
    @PostMapping("/addDormitoryInfoBatch")
    @Log(title = "excel批量增加宿舍信息", businessType = BusinessType.INSERT)
    public Result batchAddDormitoryInfo(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DormitoryInfo.class,
                    new ExcelListener<DormitoryInfo>(
                            list -> dormitoryInfoService.addBatchDormitoryInfo((List<DormitoryInfo>)list)
                    )).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("批量添加学生成功", null);
    }
    /**
     * 删除宿舍信息
     */
    @PreAuthorize("hasAnyAuthority('campus:dormitory:remove')")
    @Log(title = "删除宿舍信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteDormitoryInfo/{dormitoryIds}")
    public Result deleteDormitoryInfo(@PathVariable Long[] dormitoryIds) {
        return dormitoryInfoService.removeBatchByIds(Arrays.asList(dormitoryIds)) ? Result.success("删除宿舍信息成功", null) : Result.fail("删除宿舍信息失败");
    }
    /**
     * 修改宿舍信息
     */
    @PreAuthorize("hasAnyAuthority('campus:dormitory:edit')")
    @Log(title = "修改宿舍信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updateDormitoryInfo")
    public Result updateDormitoryInfo(@Validated @RequestBody DormitoryInfo dormitoryInfo) {
        return dormitoryInfoService.update(dormitoryInfo);
    }
    /**
     * 批量导出宿舍信息
     */
    @PreAuthorize("hasAnyAuthority('campus:dormitory:export')")
    @Log(title = "批量导出宿舍信息", businessType = BusinessType.EXPORT)
    @PostMapping("/exportDormitoryInfo")
    public void exportDormitoryInfo(HttpServletResponse response, @RequestBody DormitoryInfo dormitoryInfo) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("学生住宿信息", "UTF-8").replaceAll("\\+", "%20");;
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<DormitoryInfo> infoList = dormitoryInfoService.selectDormitoryInfoList(dormitoryInfo);
            EasyExcel.write(response.getOutputStream(), DormitoryInfo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("学生住宿信息").doWrite(infoList);
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
    /**
     * 获取当前用户的宿舍信息
     */
    @Log(title = "获取当前用户的宿舍信息", businessType = BusinessType.QUERY)
    @GetMapping("/getSelfDormitoryInfo")
    public Result getCurrentUserDormitoryInfo() {
        return dormitoryInfoService.getCurrentUserDormitoryInfo();
    }
}