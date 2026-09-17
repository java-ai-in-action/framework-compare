# spring-ai-quickstart · Spring AI Step 1–5 完整实现

> 配套文章：篇3《Spring AI 2.0 GA 实战：从 0 到 1 搭建企业级 ChatClient（含包迁移避坑）》

## 文件与文章的 Step 对照

| Step | 能力 | 对应文件 | 接口 |
|---|---|---|---|
| Step 1 | 最小可跑 ChatClient | `DemoApplication.java` | `GET /chat`、`GET /chat/structured` |
| Step 2 | ChatMemory 多轮对话 | `config/ChatConfig.java` + `controller/AdvancedController.java` | `GET /api/chat/multi?sessionId=u1&q=...` |
| Step 3 | Function Calling | `tools/OrderTools.java` + `controller/AdvancedController.java` | `GET /api/chat/tools?q=订单A12345到哪了` |
| Step 4 | MCP Client | `application.yml`（注释段）+ `pom.xml`（注释依赖） | 取消注释即可 |
| Step 5 | 流式输出 SSE | `controller/AdvancedController.java` | `GET /api/chat/stream?q=...` |

## 快速开始

```bash
# 1. 配置 API Key（任选一家，都走 OpenAI 协议）
export OPENAI_API_KEY=sk-xxx
# export OPENAI_BASE_URL=https://api.deepseek.com        # 用 DeepSeek
# export OPENAI_BASE_URL=https://dashscope.aliyuncs.com/compatible-mode/v1  # 用通义

# 2. 启动
mvn spring-boot:run
```

## 逐个验证

```bash
# Step 1：基础对话
curl "http://localhost:8080/chat?q=用一句话介绍Spring AI"

# Step 1：结构化输出（返回 JSON 形状）
curl "http://localhost:8080/chat/structured?q=Spring AI"

# Step 2：多轮对话（同一个 sessionId 记住上下文）
curl "http://localhost:8080/api/chat/multi?sessionId=u1&q=我叫老板"
curl "http://localhost:8080/api/chat/multi?sessionId=u1&q=我叫什么"   # 模型应能答出"老板"

# Step 3：Function Calling（模型自动调用 OrderTools）
curl "http://localhost:8080/api/chat/tools?q=订单A12345到哪了"

# Step 5：流式输出（逐字返回）
curl -N "http://localhost:8080/api/chat/stream?q=写一首关于咖啡的四行诗"
```

## 开启 Step 4（MCP Client）

1. `pom.xml`：取消 `spring-ai-starter-mcp-client` 依赖的注释
2. `application.yml`：取消 `spring.ai.mcp` 配置段的注释
3. 本地 stdio 方式需先装 Node.js；远程方式直接填 MCP Server 的 URL

## 注意事项

- **记忆是内存版**：重启丢失。生产请换 `JdbcChatMemory`（PostgreSQL）或 Redis 实现
- **端口**：本模块用 8080，与 langchain4j（8081）、saa（8082）错开，可同时运行
- **版本号**：`pom.xml` 中的 Spring Boot / Spring AI 版本号标了 TODO，请以官方 GA 为准
