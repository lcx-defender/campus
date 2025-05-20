package com.lcx.campus.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.lcx.campus.domain.dto.PageQuery;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * <p>
 * 学生宿舍信息表
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dormitory_info")
public class DormitoryInfo extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "主键ID不能为空", groups = {DormitoryInfo.update.class})
    @ExcelIgnore
    private Long Id;
    /**
     * 学号
     */
    @NotNull(message = "学号不能为空", groups = {DormitoryInfo.insert.class, DormitoryInfo.update.class})
    @ExcelProperty(value = "学号")
    @ToolParam(required = false, description = "学生学号")
    private String studentId;
    /**
     * 宿舍楼号
     */
    @NotNull(message = "宿舍楼号不能为空", groups = {DormitoryInfo.insert.class, DormitoryInfo.update.class})
    @ExcelProperty(value = "宿舍楼号")
    @ToolParam(required = false, description = "宿舍楼号")
    private String dormitoryId;
    /**
     * 房间号
     */
    @ExcelProperty(value = "房间号")
    @NotNull(message = "房间号不能为空", groups = {DormitoryInfo.insert.class, DormitoryInfo.update.class})
    @ToolParam(required = false, description = "房间号")
    private String roomId;
    /**
     * 床位号
     */
    @ExcelProperty(value = "床位号")
    @NotNull(message = "床位号不能为空", groups = {DormitoryInfo.insert.class, DormitoryInfo.update.class})
    @ToolParam(required = false, description = "床位号")
    private String bedId;
    /**
     * 学生姓名
     */
    @TableField(exist = false)
    @ExcelProperty(value = "学生姓名")
    @ToolParam(required = false, description = "学生姓名")
    private String studentName;

    public interface insert {}
    public interface update {}
}
