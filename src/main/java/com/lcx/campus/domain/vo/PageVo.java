package com.lcx.campus.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 分页查询结果
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
public class PageVo<T> {
    // 总条数
    private Long total;
    // 总页数
    private Long pages;
    // 当前分页数据
    private List<T> list;

    /**
     * 将mybatis-plus的分页对象转换为自定义的分页对象, 并将分页对象中的数据转换为指定类型的数据
     * @param page
     * @param clazz 指定转化的类型
     * @return
     * @param <P>
     * @param <VO>
     */
    public static <P, VO> PageVo<VO> of(Page<P> page, Class<VO> clazz) {
        PageVo<VO> pageVo = new PageVo<>();
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        List<P> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            pageVo.setList(Collections.emptyList());
        } else {
            pageVo.setList(BeanUtil.copyToList(records, clazz));
        }
        return pageVo;
    }

    public static <VO> PageVo<VO> of(Page<VO> page) {
        PageVo<VO> pageVo = new PageVo<>();
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        if(CollUtil.isEmpty(page.getRecords())) {
            pageVo.setList(Collections.emptyList());
        } else pageVo.setList(page.getRecords());
        return pageVo;
    }

    public static <P, VO> PageVo<VO> of(Page<P> page, Function<P, VO> converter) {
        PageVo<VO> pageVo = new PageVo<>();
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        List<P> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            pageVo.setList(Collections.emptyList());
        } else {
            pageVo.setList(records.stream().map(converter).collect(Collectors.toList()));
        }
        return pageVo;

    }
}
