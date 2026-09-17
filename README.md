# framework-compare · 三大 Java AI 框架同台对比

> 配套文章：篇2《Spring AI vs LangChain4j vs Spring AI Alibaba：2026 终极选型指南》
>
> 一个仓库跑通三个框架的最小可运行示例，用同一套场景对比代码量、配置方式与运行表现。

## 📦 包含什么

```
framework-compare/
├── spring-ai-quickstart/       # Spring AI 2.0：Step 1-5 完整实现（10 行 Demo → 记忆 → 工具 → MCP → SSE）
├── langchain4j-quickstart/     # LangChain4j 1.20 最小可跑（AiService + @V 模板变量）
├── saa-quickstart/             # Spring AI Alibaba 1.0 最小可跑（百炼 starter）
├── benchmarks/                 # 6 维度实测脚本（模型集成/向量库/MCP/Agent/可观测/社区）
└── docker-compose.yml          # 一键起 pgvector + Qdrant + Redis 8
```

## 📄 spring-ai-quickstart 的 Step 对照（配套篇3）

| Step | 能力 | 文件 |
|---|---|---|
| 1 | 最小可跑 ChatClient | `DemoApplication.java` |
| 2 | ChatMemory 多轮对话 | `config/ChatConfig.java` |
| 3 | Function Calling | `tools/OrderTools.java` |
| 4 | MCP Client | `application.yml`（注释段） |
| 5 | 流式输出 SSE | `controller/AdvancedController.java` |

细节见 [`spring-ai-quickstart/README.md`](./spring-ai-quickstart/README.md)。

## 🚀 快速开始

### 0. 起基础设施

```bash
docker-compose up -d
# 起 pgvector:5432 / qdrant:6333 / redis:6379
```

### 1. 配置模型 API Key（任选其一）

```bash
export OPENAI_API_KEY=sk-xxx        # OpenAI 协议兼容（DeepSeek/通义均可）
export DEEPSEEK_API_KEY=sk-xxx      # DeepSeek
export DASHSCOPE_API_KEY=sk-xxx     # 阿里云百炼
```

### 2. 跑 Spring AI 2.0

```bash
cd spring-ai-quickstart
mvn spring-boot:run
curl "http://localhost:8080/chat?q=用一句话介绍Spring AI"
curl "http://localhost:8080/api/chat/tools?q=订单A12345到哪了"    # Function Calling
curl -N "http://localhost:8080/api/chat/stream?q=写一首关于咖啡的四行诗"  # SSE
```

### 3. 跑 LangChain4j 1.20

```bash
cd langchain4j-quickstart
mvn spring-boot:run
curl "http://localhost:8081/chat?q=用一句话介绍LangChain4j"
```

### 4. 跑 Spring AI Alibaba

```bash
cd saa-quickstart
mvn spring-boot:run
curl "http://localhost:8082/chat?q=用一句话介绍SAA"
```

## 📊 6 维度实测结论速览

| 维度 | 最强 | 说明 |
|---|---|---|
| 模型集成数量 | LangChain4j | 30+ 模型开箱即用 |
| 向量库支持 | LangChain4j | 覆盖最广，差异 <10% |
| MCP 支持 | Spring AI 2.0 | `@McpTool` 注解 47 行暴露 10 个方法 |
| 多 Agent 编排 | Spring AI Alibaba | Graph + Mermaid 可视化 |
| 可观测 | Spring AI / SAA | Micrometer + OTel 原生集成 |
| 中文社区 | SAA | 官方中文文档 + 国内生态最全 |

详细数据见 `benchmarks/` 与文章。

## ⚠️ Known Pitfalls（踩坑清单）

1. **别追 RC 版本**——锁定 GA，Milestone/RC 的 API 可能突变
2. **Spring AI 2.0 强制 JDK 21 + Spring Boot 4**——老项目升级前先看本仓库 `spring-ai-quickstart` 的 pom
3. **MCP 包名迁移**：`io.modelcontextprotocol.*` → `org.springframework.ai.mcp.*`
4. **LangChain4j 中文资料滞后官方 1–2 个月**——以 GitHub README + Javadoc 为准
5. **国内合规**：生产环境优先通义 / DeepSeek / 豆包，开源模型走 Ollama / vLLM

## 🗺️ Roadmap

- [x] v0.1 三框架 hello world + docker-compose
- [x] v0.2 spring-ai-quickstart 补齐 Step 2-5（记忆/工具/SSE）
- [ ] v0.3 RAG 对比（同文档、同问题、三框架召回效果）
- [ ] v0.4 多 Agent 编排对比
- [ ] v1.0 基准测试报告（压测 + Token 成本）

## License

MIT
