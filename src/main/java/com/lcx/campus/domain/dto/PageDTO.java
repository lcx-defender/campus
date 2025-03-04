package com.lcx.campus.domain.dto;

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
public class PageDTO<T> {
    // 总条数
    private Long total;
    // 总页数
    private Long pages;
    // 当前分页数据
    private List<T> list;

    /**
     * 将mybatis-plus的分页对象转换为自定义的分页对象, 并将分页对象中的数据转换为指定类型的数据
     * @param page
     * @param clazz
     * @return
     * @param <P>
     * @param <DTO>
     */
    public static <P, DTO> PageDTO<DTO> of(Page<P> page, Class<DTO> clazz) {
        PageDTO<DTO> pageDTO = new PageDTO<>();
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        List<P> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            pageDTO.setList(Collections.emptyList());
        } else {
            pageDTO.setList(BeanUtil.copyToList(records, clazz));
        }
        return pageDTO;
    }

    public static <DTO> PageDTO<DTO> of(Page<DTO> page) {
        PageDTO<DTO> pageDTO = new PageDTO<>();
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        if(CollUtil.isEmpty(page.getRecords())) {
            pageDTO.setList(Collections.emptyList());
        } else pageDTO.setList(page.getRecords());
        return pageDTO;
    }

    public static <P, DTO> PageDTO<DTO> of(Page<P> page, Function<P, DTO> converter) {
        PageDTO<DTO> pageDTO = new PageDTO<>();
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        List<P> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            pageDTO.setList(Collections.emptyList());
        } else {
            pageDTO.setList(records.stream().map(converter).collect(Collectors.toList()));
        }
        return pageDTO;

    }
}
