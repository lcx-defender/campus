package com.lcx.campus.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.domain.po.DictData;
import com.lcx.campus.domain.po.DictType;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.enums.DictStatus;
import com.lcx.campus.mapper.DictDataMapper;
import com.lcx.campus.mapper.DictTypeMapper;
import com.lcx.campus.service.IDictTypeService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.lcx.campus.constant.RedisConstants.SYS_DICT_KEY;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private DictDataMapper dictDataMapper;

    @Override
    public Result pageList(DictType dictType) {
        Page<DictType> dictPage = dictType.toMpPage();
        Page<DictType> page = lambdaQuery()
                .like(dictType.getDictName() != null, DictType::getDictName, dictType.getDictName())
                .like(dictType.getDictType() != null, DictType::getDictType, dictType.getDictType())
                .page(dictPage);
        PageVo<DictType> dicts = PageVo.of(page);
        return Result.success(dicts);
    }

    @Override
    public Result addDictType(DictType dictType) {
        // 检查字典类型是否存在
        if(isExist(dictType)) {
            return Result.fail("字典类型已存在");
        }
        dictType.setCreateTime(LocalDateTime.now());
        save(dictType);
        return Result.success("添加成功");
    }

    @Override
    public void refreshCache() {
        // 清空缓存
        clearDictCache();
        // 重新加载缓存
        loadingDictCache();
    }

    @Override
    public void loadingDictCache() {
        // 查询所有字典类型
        List<DictType> dictTypes = list();
        // 遍历字典类型，加载到缓存
        for (DictType dictType : dictTypes) {
            // 查询对应的字典数据,type类型对应且状态正常
            String type = dictType.getDictType();
            String dictStatus = DictStatus.NORMAL.getCode();
            List<DictData> dictDataList = dictDataMapper.selectListByDictTypeWithStatus(type, dictStatus);
            stringRedisTemplate.opsForValue().set(SYS_DICT_KEY + type, JSON.toJSONString(dictDataList));
        }
    }

    @Override
    public void clearDictCache() {
        // 清空缓存逻辑
        Set<String> keys = stringRedisTemplate.keys(SYS_DICT_KEY + "*");
        keys.forEach(key -> stringRedisTemplate.delete(key));
    }

    private boolean isExist(DictType dictType) {
        return lambdaQuery()
                .eq(DictType::getDictName, dictType.getDictName())
                .or()
                .eq(DictType::getDictType, dictType.getDictType())
                .count() > 0;
    }
}
