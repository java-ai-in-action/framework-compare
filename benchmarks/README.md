# benchmarks · 6 维度实测

> 配套文章：篇2《Spring AI vs LangChain4j vs Spring AI Alibaba：2026 终极选型指南》

## 目录规划

| 脚本 | 维度 | 状态 |
|---|---|---|
| `model-integration/` | 模型集成数量对比 | 🚧 TODO |
| `vector-store/` | 向量库接入代码量对比 | 🚧 TODO |
| `mcp-expose/` | MCP Server 暴露代码行数对比 | 🚧 TODO |
| `multi-agent/` | 4 Agent 协同（Planner/Executor/Reviewer/Summarizer）代码量对比 | 🚧 TODO |
| `observability/` | Micrometer / OTel / Langfuse 接入对比 | 🚧 TODO |
| `load-test/` | 虚拟线程 vs asyncio 压测 | 🚧 TODO |

## 数据快照（随文章持续更新）

| 维度 | Spring AI 2.0 | LangChain4j 1.20 | SAA 1.0 |
|---|---|---|---|
| MCP 暴露 10 方法 | 47 行 | ~80 行 | 47 行 + Nacos 注册 |
| 4 Agent 电商客服 | ~300 行 | ~220 行（+LangGraph4j） | ~80 行（Graph） |
| 切换模型成本 | 3 行 + 1 个 yml | 同级 | 同级 |
