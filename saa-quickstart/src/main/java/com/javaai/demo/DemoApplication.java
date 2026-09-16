package com.javaai.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring AI Alibaba 最小可跑示例
 *
 * SAA 兼容 Spring AI 的 ChatClient API，所以代码与 spring-ai-quickstart 几乎一致；
 * 差异在 starter：spring-ai-alibaba-starter-dashscope 直连百炼，配置更省事。
 *
 *   GET /chat?q=...   基础对话（通义 qwen-plus）
 *
 * 配套文章：篇2《Spring AI vs LangChain4j vs Spring AI Alibaba：2026 终极选型指南》
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    static class ChatController {

        private final ChatClient chatClient;

        public ChatController(ChatClient.Builder builder) {
            this.chatClient = builder
                    .defaultSystem("你是一个简洁的 Java AI 技术助手，回答不超过 100 字")
                    .build();
        }

        @GetMapping("/chat")
        public String chat(@RequestParam String q) {
            return chatClient.prompt().user(q).call().content();
        }
    }
}
