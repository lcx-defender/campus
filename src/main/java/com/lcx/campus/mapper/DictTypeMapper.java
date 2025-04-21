package com.lcx.campus.mapper;

import com.lcx.campus.domain.DictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 字典类型表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface DictTypeMapper extends BaseMapper<DictType> {

    DictType selectByDictType(String dictType);
}
