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
 * 宿舍床位表
 * </p>
 *
 * @author 刘传星
 * @since 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dorm_bed")
public class DormBed implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 床位ID，主键
     */
    @TableId(value = "bed_id", type = IdType.AUTO)
    private Long bedId;

    /**
     * 所属房间ID（关联 dorm_room 表）
     */
    private Long roomId;

    /**
     * 床位号（如：1号床, 2号床, 3号床）
     */
    private Integer bedNo;

    /**
     * 入住学生ID（关联 sys_user 或 student 表的 userId，为空表示该床位未分配）
     */
    private Long studentId;

    /**
     * 床位状态（0空闲 1已入住 2被锁定/维修中）
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
     * 备注（如：上铺/下铺，靠窗等）
     */
    private String remark;


}
