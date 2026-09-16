package com.javaai.demo;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * LangChain4j 最小可跑示例
 *
 * 核心玩法：声明式 @AiService —— 定义接口，框架动态代理实现。
 *
 *   GET /chat?q=...                      基础对话
 *   GET /chat/translate?q=...&lang=...   @UserMessage + @V 模板变量
 *
 * 配套文章：篇2《Spring AI vs LangChain4j vs Spring AI Alibaba：2026 终极选型指南》
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /** 声明式 AI Service：ChatModel Bean 由 starter 自动注入 */
    interface Assistant {

        @SystemMessage("你是一个简洁的 Java AI 技术助手，回答不超过 100 字")
        String chat(String message);

        @UserMessage("把下面这句话翻译成 {{lang}}，只给译文：{{text}}")
        String translate(@UserMessage String text, @dev.langchain4j.service.V("lang") String lang);
    }

    @RestController
    static class ChatController {

        private final Assistant assistant;

        // Assistant 接口由 AiServices 动态代理生成，直接注入即可
        public ChatController(Assistant assistant) {
            this.assistant = assistant;
        }

        @GetMapping("/chat")
        public String chat(@RequestParam String q) {
            return assistant.chat(q);
        }

        @GetMapping("/chat/translate")
        public String translate(@RequestParam String q,
                                @RequestParam(defaultValue = "英文") String lang) {
            return assistant.translate(q, lang);
        }
    }
}
