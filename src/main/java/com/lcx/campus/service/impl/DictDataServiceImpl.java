package com.lcx.campus.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.DictData;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.mapper.DictDataMapper;
import com.lcx.campus.mapper.DictTypeMapper;
import com.lcx.campus.service.IDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lcx.campus.constant.RedisConstants.SYS_DICT_KEY;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    @Resource
    private DictDataMapper dictDataMapper;

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result pageList(DictData dictData) {
        Page<DictData> queryPage = dictData.toMpPage();
        Page<DictData> resPage = lambdaQuery()
                .eq(dictData.getDictType() != null, DictData::getDictType, dictData.getDictType())
                .eq(dictData.getDictStatus() != null, DictData::getDictStatus, dictData.getDictStatus())
                .like(dictData.getDictLabel() != null, DictData::getDictLabel, dictData.getDictLabel())
                .page(queryPage);
        PageVo<DictData> res = PageVo.of(resPage);
        return Result.success(res);
    }

    @Override
    public Result getInfo(Long dictCode) {
        DictData dictData = dictDataMapper.selectById(dictCode);
        if (dictData == null) {
            return Result.fail("数据字典不存在");
        }
        return Result.success(dictData);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @Override
    public Result dictType(String dictType) {
        List<DictData> dictDataList = dictDataMapper.selectListByDictType(dictType);
        if (StringUtils.isEmpty(dictDataList)) {
            return Result.fail("数据字典不存在");
        }
        return Result.success(dictDataList);
    }

    @Override
    public Result add(DictData dictData) {
        if (isNotExistDictType(dictData.getDictType()) || isExistDictData(dictData)) {
            return Result.fail("字典数据已存在 或 字典类型不存在,新增失败");
        }
        dictDataMapper.insert(dictData);
        List<DictData> dictDataList = dictDataMapper.selectListByDictType(dictData.getDictType());
        stringRedisTemplate.opsForValue().set(SYS_DICT_KEY + dictData.getDictType(), JSON.toJSONString(dictDataList));
        return Result.success();
    }

    @Override
    public Result updateDict(DictData dictData) {
        if (isNotExistDictType(dictData.getDictType()) || isExistDictData(dictData)) {
            return Result.fail("字典数据已存在 或 字典类型不存在,修改失败");
        }
        boolean isSuccess = updateById(dictData);
        if (isSuccess) {
            List<DictData> dictDataList = dictDataMapper.selectListByDictType(dictData.getDictType());
            stringRedisTemplate.opsForValue().set(SYS_DICT_KEY + dictData.getDictType(), JSON.toJSONString(dictDataList));
            return Result.success();
        }
        return Result.fail("修改失败");
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public Result deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            DictData dictData = dictDataMapper.selectById(dictCode);
            dictDataMapper.deleteById(dictCode);
            List<DictData> dictDataList = dictDataMapper.selectListByDictType(dictData.getDictType());
            // 重置缓存
            stringRedisTemplate.opsForValue().set(SYS_DICT_KEY + dictData.getDictType(), JSON.toJSONString(dictDataList));
        }
        return Result.success();
    }


    private boolean isNotExistDictType(String dictType) {
        // 校验字典类型是否存在
        return dictTypeMapper.selectByDictType(dictType) == null;
    }

    private boolean isExistDictData(DictData dictData) {
        // 校验字典数据是否存在
        DictData existingDictData = dictDataMapper.selectOne(
                lambdaQuery()
                        .eq(DictData::getDictType, dictData.getDictType())
                        .eq(DictData::getDictValue, dictData.getDictValue())
        );
        if (StringUtils.isNull(existingDictData)) {
            return false;
        }
        return true;
    }
}
