package com.javaai.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring AI 最小可跑示例
 *
 * 三个接口：
 *   GET /chat?q=...          基础对话
 *   GET /chat/system?q=...   带系统提示词
 *   GET /chat/structured?q=... 结构化输出（返回 JSON）
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

        // ChatClient.Builder 由 Spring AI 自动装配，改配置文件即可切换模型厂商
        public ChatController(ChatClient.Builder builder) {
            this.chatClient = builder
                    .defaultSystem("你是一个简洁的 Java AI 技术助手，回答不超过 100 字")
                    .build();
        }

        /** 基础对话 */
        @GetMapping("/chat")
        public String chat(@RequestParam String q) {
            return chatClient.prompt().user(q).call().content();
        }

        /** 结构化输出：让模型按 Java Record 的形状返回 */
        public record FrameworkIntro(String name, String vendor, String slogan) {}

        @GetMapping("/chat/structured")
        public FrameworkIntro structured(@RequestParam String q) {
            return chatClient.prompt()
                    .user(u -> u.text("介绍 {framework}，按 JSON 格式给出 name/vendor/slogan")
                            .param("framework", q))
                    .call()
                    .entity(FrameworkIntro.class);
        }
    }
}
