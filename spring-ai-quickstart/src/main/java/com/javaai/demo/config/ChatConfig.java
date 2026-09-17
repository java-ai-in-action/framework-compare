package com.javaai.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Step 2：ChatMemory 多轮对话配置
 *
 * 配套文章：篇3《Spring AI 2.0 GA 实战：从 0 到 1 搭建企业级 ChatClient》
 *
 * 生产提醒：MessageWindowChatMemory 是内存实现，重启即丢。
 * 生产环境请换成 JdbcChatMemory（PostgreSQL）或基于 Redis 的实现。
 */
@Configuration
public class ChatConfig {

    /** 保留最近 20 条消息的滑动窗口记忆 */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }

    /** 带记忆 + 系统提示词的主 ChatClient */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory memory) {
        return builder
                .defaultSystem("你是一个简洁的 Java AI 技术助手，回答不超过 100 字")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }
}
