package com.lcx.campus.controller;

import com.lcx.campus.domain.vo.MessageVO;
import com.lcx.campus.repository.ChatHistoryRepository;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Security;
import java.util.List;

@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {
    @Resource
    private ChatHistoryRepository chatHistoryRepository;
    @Resource
    private ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type) {
        return chatHistoryRepository.getChatIds(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        Long userId = SecurityUtils.getUserId();
        List<Message> messages = chatMemory.get(chatId , Integer.MAX_VALUE);
        if(messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }
}
