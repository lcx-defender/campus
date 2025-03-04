package com.lcx.campus.domain.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Map;

/**
 * <p>
 * 分页查询参数
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
public class PageQuery {
    // 查询页码
    private Integer pageNo;
    // 查询条数/每页显示条数
    private Integer pageSize;
    // 排序规则, key为字段名, value为true/false, true表示升序, false表示降序
    private Map<String, Boolean> sort;

    public <T> Page<T> toMpPage() {
        Page<T> page = Page.of(pageNo, pageSize);
        if (sort != null) {
            sort.forEach((k, v) -> {
                page.addOrder(v ? OrderItem.asc(k) : OrderItem.desc(k));
            });
        } else {
            // 按照创建时间升序排列
            page.addOrder(OrderItem.asc("create_time"));
        }
        return page;
    }
}
