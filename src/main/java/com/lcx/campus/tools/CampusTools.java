package com.lcx.campus.tools;

import com.lcx.campus.domain.po.Dept;
import com.lcx.campus.domain.po.User;
import com.lcx.campus.service.IDeptService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CampusTools {

    @Resource
    private IDeptService deptService;

    @Tool(description = "查询当前用户的学校Id", returnDirect = false)
    public Long getCurrentUserUniversityId(@ToolParam(description = "查询学校名称") Dept dept) {
        User user = SecurityUtils.getLoginUser().getUser();
        return deptService.getUniversityIdByUserId(user.getUserId());
    }
}