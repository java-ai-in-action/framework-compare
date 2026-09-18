# benchmarks · 实测与评估工具

> 配套文章：篇2《三框架选型》/ 篇4《RAG 从 32% 到 89%》

## 📁 目录

| 目录 | 内容 | 状态 |
|---|---|---|
| [`rag/`](./rag) | **RAG 检索评估工具包**：90 篇知识库 + 100 题测试集 + Hit Rate@K 评估器 | ✅ 可跑 |
| `model-integration/` | 模型集成数量对比 | 🚧 TODO |
| `vector-store/` | 向量库接入代码量对比 | 🚧 TODO |
| `mcp-expose/` | MCP Server 暴露代码行数对比 | 🚧 TODO |
| `multi-agent/` | 4 Agent 协同代码量对比 | 🚧 TODO |
| `observability/` | Micrometer / OTel / Langfuse 接入对比 | 🚧 TODO |
| `load-test/` | 虚拟线程 vs asyncio 压测 | 🚧 TODO |

## 🚀 rag/ 快速开始

```bash
cd rag
mvn -q compile exec:java -Dexec.args="3"
```

实测结果（Hit Rate@3）：仅向量 82.2% / 仅关键词 57.8%@1 / **混合+RRF 96.7%**

详见 [`rag/README.md`](./rag/README.md)。

## 📊 数据快照（随文章持续更新）

| 维度 | Spring AI 2.0 | LangChain4j 1.20 | SAA 1.0 |
|---|---|---|---|
| MCP 暴露 10 方法 | 47 行 | ~80 行 | 47 行 + Nacos 注册 |
| 4 Agent 电商客服 | ~300 行 | ~220 行（+LangGraph4j） | ~80 行（Graph） |
| 切换模型成本 | 3 行 + 1 个 yml | 同级 | 同级 |
