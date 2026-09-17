package com.javaai.demo.controller;

import com.javaai.demo.tools.OrderTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Step 2 / 3 / 5 的进阶接口
 *
 * 配套文章：篇3《Spring AI 2.0 GA 实战：从 0 到 1 搭建企业级 ChatClient》
 *
 *   GET /api/chat/multi?sessionId=u1&q=...   Step 2 多轮对话（按会话隔离记忆）
 *   GET /api/chat/tools?q=...                Step 3 Function Calling（模型自动调工具）
 *   GET /api/chat/stream?q=...               Step 5 流式输出 SSE
 *
 * Step 4（MCP Client）见 application.yml 中注释掉的 mcp 配置段。
 */
@RestController
@RequestMapping("/api")
public class AdvancedController {

    private final ChatClient chatClient;
    private final OrderTools orderTools;

    public AdvancedController(ChatClient chatClient, OrderTools orderTools) {
        this.chatClient = chatClient;
        this.orderTools = orderTools;
    }

    /**
     * Step 2：多轮对话
     * 同一个 sessionId 共享上下文，不同 sessionId 互不干扰。
     */
    @GetMapping("/chat/multi")
    public String multi(@RequestParam String sessionId, @RequestParam String q) {
        return chatClient.prompt()
                .user(q)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();
    }

    /**
     * Step 3：Function Calling
     * 问"订单 A12345 到哪了"时，模型会自动调用 OrderTools.queryOrderStatus。
     */
    @GetMapping("/chat/tools")
    public String tools(@RequestParam String q) {
        return chatClient.prompt()
                .user(q)
                .tools(orderTools)
                .call()
                .content();
    }

    /**
     * Step 5：流式输出（SSE）
     * 前端用 EventSource 接收，边生成边显示。
     */
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String q) {
        return chatClient.prompt()
                .user(q)
                .stream()
                .content();
    }
}
