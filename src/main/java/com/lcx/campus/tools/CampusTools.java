package com.lcx.campus.tools;

import com.lcx.campus.domain.DormitoryInfo;
import com.lcx.campus.domain.User;
import com.lcx.campus.service.IDormitoryInfoService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CampusTools {

    @Resource
    private IDormitoryInfoService dormitoryInfoService;

    /**
     * returnDirect = false表示把查询结果直接返回给大模型
     */
    @Tool(description = "查询学生宿舍信息", returnDirect = false)
    public List<DormitoryInfo> queryDormitoryInfo(@ToolParam(description = "查询学生住宿的条件") DormitoryInfo dormitoryInfo) {
        return dormitoryInfoService.selectDormitoryInfoList(dormitoryInfo);
    }
}
