# langchain4j-quickstart · LangChain4j 1.20 最小可跑

> 配套文章：篇2《Spring AI vs LangChain4j vs Spring AI Alibaba：2026 终极选型指南》

## ✨ 特点

- **声明式 `@AiService`**：定义接口，框架动态代理实现，接口即文档
- **模型集成最广**：30+ 模型开箱即用（OpenAI / Bedrock / Gemini / Ollama…）
- **响应式流式**：默认 `Flux<String>` 流式返回

## 📁 文件说明

| 文件 | 说明 |
|---|---|
| `src/main/java/com/javaai/demo/DemoApplication.java` | 主类 + `Assistant` 接口 + Controller |
| `src/main/resources/application.yml` | OpenAI 协议配置（可切 DeepSeek / 通义） |
| `pom.xml` | `langchain4j-bom` + `langchain4j-open-ai-spring-boot-starter` |

## 🔌 接口

| 接口 | 说明 |
|---|---|
| `GET /chat?q=...` | 基础对话（带系统提示词） |
| `GET /chat/translate?q=...&lang=...` | `@UserMessage` + `@V` 模板变量翻译 |

## 🚀 快速开始

```bash
# 1. 配置 API Key
export OPENAI_API_KEY=sk-xxx
# 可选：切模型
# export OPENAI_BASE_URL=https://api.deepseek.com/v1   # DeepSeek

# 2. 启动（端口 8081，与 spring-ai 8080 / saa 8082 错开）
mvn spring-boot:run
```

### 验证

```bash
curl "http://localhost:8081/chat?q=用一句话介绍LangChain4j"
curl "http://localhost:8081/chat/translate?q=你好世界&lang=英文"
```

## 🆚 与 Spring AI 的核心差异

| 维度 | LangChain4j | Spring AI |
|---|---|---|
| 编程模型 | 声明式接口（`@AiService`） | 链式 Builder（`ChatClient`） |
| 模型集成 | 30+ 最广 | ~20 |
| 可观测 | ⚠️ 需自定义 | ✅ 原生 Micrometer / OTel |
| 学习曲线 | 接口即文档，上手快 | 需理解 Advisor 链 |

**一句话**：快速验证 / 多模型试验选 LangChain4j；Spring 深度集成 / 企业级可观测选 Spring AI。

## License

MIT
