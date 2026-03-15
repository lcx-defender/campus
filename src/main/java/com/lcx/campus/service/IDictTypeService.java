package com.lcx.campus.service;

import com.lcx.campus.domain.po.DictType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.vo.Result;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IDictTypeService extends IService<DictType> {

    Result pageList(DictType dictType);

    Result addDictType(DictType dictType);

    void refreshCache();

    void loadingDictCache();

    void clearDictCache();
}
