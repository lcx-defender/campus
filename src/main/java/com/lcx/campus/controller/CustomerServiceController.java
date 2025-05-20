package com.lcx.campus.controller;

import com.lcx.campus.repository.ChatHistoryRepository;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * 智能问答客服
 *
 * @author 刘传星
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/ai-service")
public class CustomerServiceController {
    @Resource
    private ChatClient serviceChatClient;
    @Resource
    private ChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/service", produces = "text/html;charset=utf-8")
    public Flux<String> service(String prompt, String chatId) {
        // 1.保存会话id
        chatHistoryRepository.save("service", chatId);
        // 2.请求模型
        return serviceChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }
}
