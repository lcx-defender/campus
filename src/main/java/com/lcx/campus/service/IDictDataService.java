package com.lcx.campus.service;

import com.lcx.campus.domain.po.DictData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.vo.Result;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IDictDataService extends IService<DictData> {

    Result pageList(DictData dictData);

    Result getInfo(Long dictCode);

    /**
     * 根据字典类型查询字典数据信息
     */
    Result dictType(String dictType);

    /**
     * 新增字典数据
     */
    Result add(DictData dictData);

    Result updateDict(DictData dictData);

    Result deleteDictDataByIds(Long[] dictCodes);
}
