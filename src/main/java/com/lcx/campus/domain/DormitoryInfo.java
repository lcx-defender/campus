package com.lcx.campus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
public class DormitoryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableId(value = "student_id", type = IdType.INPUT)
    private String studentId;

    /**
     * 宿舍楼号
     */
    private String dormitoryId;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 床位号
     */
    private String bedId;


}
