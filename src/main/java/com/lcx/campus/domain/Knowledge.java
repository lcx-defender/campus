package com.lcx.campus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *     知识库表
 * </p>
 *
 * @author 刘传星
 * @since 2025-05-27
 */
@Data
@TableName(value = "knowledge")
public class Knowledge {
    /**
     * 知识库ID
     */
    @TableId(type = IdType.AUTO)
    private Long fileId;
    /**
     * 所属部门
     */
    private Long deptId;
    /**
     * 文件存储路径
     */
    private String fileUrl;
    /**
     * 知识库描述
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}