package com.lcx.campus.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 宿舍楼栋表
 * </p>
 *
 * @author 刘传星
 * @since 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dorm_building")
public class DormBuilding implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 楼栋ID，主键
     */
    @TableId(value = "building_id", type = IdType.AUTO)
    private Long buildingId;

    /**
     * 所属高校ID（关联 dept 院校表的主键）
     */
    private Long universityId;

    /**
     * 楼栋名称（如：学一栋、梅苑A栋）
     */
    private String buildingName;

    /**
     * 所属校区（如：主校区、南校区，供同一高校有多个校区时使用）
     */
    private String campusName;

    /**
     * 楼栋性别限制（0:混住/未定, 1:男生宿舍, 2:女生宿舍）
     */
    private Integer buildingGender;

    /**
     * 总楼层数
     */
    private Integer totalFloors;

    /**
     * 楼栋状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表逻辑删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注（如：靠近南门、无电梯等）
     */
    private String remark;


}
