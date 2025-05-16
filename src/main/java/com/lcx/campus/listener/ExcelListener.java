package com.lcx.campus.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Excel监听器
 *
 * @author 刘传星
 * @since 2025-05-16
 */
@Slf4j
public class ExcelListener<T> implements ReadListener<T> {

    private static final int BATCH_COUNT = 1000;
    private List<T> cachedDataList = new ArrayList<>(BATCH_COUNT);
    private Predicate<T> predicate;
    private Consumer<Collection<T>> consumer;

    public ExcelListener(Predicate<T> predicate, Consumer<Collection<T>> consumer) {
        this.predicate = predicate;
        this.consumer = consumer;
    }

    public ExcelListener(Consumer<Collection<T>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        if (predicate != null && !predicate.test(data)) {
            return;
        }
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            try {
                consumer.accept(cachedDataList);
            } catch (Exception e) {
                log.error("数据上传失败：{}", cachedDataList);
                throw new RuntimeException("excel 导入失败");
            }
            cachedDataList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (!cachedDataList.isEmpty()) {
            try {
                consumer.accept(cachedDataList); // 处理剩余数据
                log.info("所有excel数据都已经解析完毕");
            } catch (Exception e) {
                log.error("数据上传失败：{}", cachedDataList);
                throw new RuntimeException("数据上传失败：" + cachedDataList);
            }
            cachedDataList.clear();
        }
    }
}
