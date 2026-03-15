package com.lcx.campus.mapper;

import com.lcx.campus.domain.po.DictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 字典数据表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface DictDataMapper extends BaseMapper<DictData> {

    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType}")
    List<DictData> selectListByDictType(String dictType);

    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{type} AND dict_status = #{dictStatus}")
    List<DictData> selectListByDictTypeWithStatus(String type, String dictStatus);
}
