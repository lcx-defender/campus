package com.lcx.campus.domain;

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
    @NotNull(message = "主键ID不能为空")
    private Long Id;

    /**
     * 学号
     */
    @NotNull(message = "学号不能为空", groups = {DormitoryInfo.insert.class})
    private String studentId;

    /**
     * 宿舍楼号
     */
    @NotNull(message = "宿舍楼号不能为空", groups = {DormitoryInfo.insert.class})
    private String dormitoryId;

    /**
     * 房间号
     */
    @NotNull(message = "房间号不能为空", groups = {DormitoryInfo.insert.class})
    private String roomId;

    /**
     * 床位号
     */
    @NotNull(message = "床位号不能为空", groups = {DormitoryInfo.insert.class})
    private String bedId;

    /**
     * 查询条件:部门编号
     */
    @TableField(exist = false)
    private Long deptId;

    public interface insert {}

}
