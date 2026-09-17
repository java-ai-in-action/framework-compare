# saa-quickstart · Spring AI Alibaba 1.0 最小可跑

> 配套文章：篇2《Spring AI vs LangChain4j vs Spring AI Alibaba：2026 终极选型指南》
> 篇6《我用 Spring AI Alibaba + Nacos 搭了个企业级 Multi-Agent》（规划中）

## ✨ 特点

- **兼容 Spring AI API**：代码与 `spring-ai-quickstart` 几乎一致，切换成本极低
- **直连百炼**：DashScope starter，通义 qwen-plus 开箱即用
- **国内生态**：Nacos / Higress / Sentinel / RocketMQ / ARMS 深度集成

## 📁 文件说明

| 文件 | 说明 |
|---|---|
| `src/main/java/com/javaai/demo/DemoApplication.java` | 主类 + ChatClient Controller |
| `src/main/resources/application.yml` | DashScope 配置（qwen-plus） |
| `pom.xml` | `spring-ai-alibaba-starter-dashscope` |

## 🔌 接口

| 接口 | 说明 |
|---|---|
| `GET /chat?q=...` | 基础对话（通义 qwen-plus） |

## 🚀 快速开始

```bash
# 1. 配置百炼 API Key（https://bailian.console.aliyun.com/ 申请）
export DASHSCOPE_API_KEY=sk-xxx

# 2. 启动（端口 8082）
mvn spring-boot:run
```

### 验证

```bash
curl "http://localhost:8082/chat?q=用一句话介绍Spring AI Alibaba"
```

## 🤔 什么时候选 SAA

- ✅ 团队已在阿里云栈（Nacos / Sentinel / RocketMQ）
- ✅ 需要国产模型 + 国内合规（数据不出境）
- ✅ 要做多 Agent（Graph 多智能体框架 + JManus + Nacos MCP Registry）
- ❌ 纯国际部署 / 非 Spring 技术栈 → 看 Spring AI / LangChain4j

## 📚 SAA 独家能力（本仓库未覆盖，篇6 展开）

- **Graph 多智能体框架**：嵌套 / 并行 Graph、流程快照、Mermaid 可视化
- **JManus**：通用 AI Agent 平台，零代码生成工程
- **Nacos MCP Registry**：存量 Spring Cloud / Dubbo 应用零代码改造发布 MCP

## License

MIT
