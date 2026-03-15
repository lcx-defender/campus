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
 * 宿舍房间表
 * </p>
 *
 * @author 刘传星
 * @since 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dorm_room")
public class DormRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房间ID，主键
     */
    @TableId(value = "room_id", type = IdType.AUTO)
    private Long roomId;

    /**
     * 所属楼栋ID（关联 dorm_building 表）
     */
    private Long buildingId;

    /**
     * 所在楼层号（如：1, 2, 3）
     */
    private Integer floorNum;

    /**
     * 房间号（如：101、204B）
     */
    private String roomNo;

    /**
     * 房间类型/几人间（如：4代表四人间，6代表六人间）
     */
    private Integer roomType;

    /**
     * 房间状态（0正常 1维修中 2停用）
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
     * 备注（如：向阳、带独立卫浴等）
     */
    private String remark;


}
