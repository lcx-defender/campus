package com.lcx.campus.repository;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 聊天记录持久化
 *
 * @author 刘传星
 * @since 2025-05-18
 */
@Component
public interface ChatHistoryRepository {
    /**
     * 保存会话记录
     * @param type 业务类型，如：chat、service、pdf
     * @param chatId 会话ID
     */
    void save(String type, String chatId);

    /**
     * 获取会话ID列表
     * @param type 业务类型，如：chat、service、pdf
     * @return 会话ID列表
     */
    List<String> getChatIds(String type);
}
