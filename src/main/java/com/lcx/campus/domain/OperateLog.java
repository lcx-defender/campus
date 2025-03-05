package com.lcx.campus.domain;

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
 * 操作日志记录
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_operate_log")
public class OperateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId(value = "operate_id", type = IdType.AUTO)
    private Long operateId;

    /** 操作模块 */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 4查询 5授权 6导出 7导入 8强退）数据字典
     */
    private Integer businessType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式,PUT,POST,GET,DELETE
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）数据字典
     */
    private Integer operatorType;

    /**
     * 操作人员ID
     */
    private Long userId;

    /**
     * 请求URL
     */
    private String operateUrl;

    /**
     * 主机地址
     */
    private String operateIp;

    /**
     * 操作地点
     */
    private String operateLocation;

    /**
     * 请求参数
     */
    private String operateParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer operateStatus;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 消耗时间
     */
    private Long costTime;


}
