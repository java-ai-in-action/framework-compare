# rag · RAG 检索评估工具包

> 配套文章：篇4《RAG 准确率从 32% 干到 89%：重写切片、混合检索与 Rerank 的全流程》

## 📦 包含什么

| 文件 | 说明 |
|---|---|
| `docs-sample.json` | 90 篇示例知识库（30 篇权威文档 + 60 篇干扰文档：旧版本 / 近似主题） |
| `testset-100.json` | 100 题分层测试集（事实型 40 / 规则型 30 / 多跳 20 / 无答案 10） |
| `HybridRetriever.java` | 混合检索（向量 + 关键词）→ RRF 融合 → Rerank 精排 |
| `RagEvaluator.java` | 评估器：输出分层 Hit Rate@K |
| `pom.xml` | 独立可运行（仅依赖 Jackson，**无需 API Key**） |

> 💡 为什么知识库里要有"干扰文档"？因为真实企业知识库就是这样：同一主题有多个版本、多个近似文档，检索必须找到**正确的那一篇**。这是 RAG 最容易翻车的地方。

## 🚀 快速开始

```bash
cd benchmarks/rag

mvn -q compile exec:java                 # 默认 Hit Rate@5
mvn -q compile exec:java -Dexec.args="3" # Hit Rate@3
```

**实测输出（K=3）：**

```
=== RAG 评估结果 · Hit Rate@3 ===
类别               命中     总数        命中率
--------------------------------------
factual          39     40      97.5%
rule             28     30      93.3%
multi_hop        20     20     100.0%
--------------------------------------
总计               87     90      96.7%
```

## 📊 混合检索 vs 单路（本工具实测）

在 90 篇（含 60 篇干扰）的知识库上：

| 检索方案 | Hit Rate@1 | Hit Rate@3 | Hit Rate@5 |
|---|---|---|---|
| 仅向量召回 | 82.2% | 82.2% | 95.6% |
| 仅关键词召回 | 57.8% | 91.1% | 95.6% |
| **混合 + RRF** | 83.3% | **96.7%** | **97.8%** |

**结论**：
- 单路召回各有短板（关键词路在 @1 只有 57.8%）
- **混合 + RRF 在所有 K 值上都最优**
- K 越小（要求越严），混合的优势越明显

> ⚠️ 注意：这是 90 篇小语料 + 简化模拟检索的结果，绝对值偏高。真实企业语料（数千篇）单路召回会更差，混合的提升幅度更大。**数据不通用，方法论通用。**

## 🧠 这套代码想说明什么

1. **混合检索 > 单一检索**：向量召回擅长语义、关键词召回擅长精确匹配，两者互补
2. **RRF 融合比加权平均更稳**：只看排名，不用调分数权重
3. **召回要广、精排要准**：召回 top-50 → 精排 top-5
4. **评估驱动优化**：没有测试集，所有"优化"都是玄学

## 🔌 接入你的真实检索

把 `HybridRetriever` 替换成你的实现即可：

```java
// 1. 向量召回（Spring AI）
List<Document> vecHits = vectorStore.similaritySearch(
        SearchRequest.builder().query(q).topK(50).build());

// 2. 关键词召回（Elasticsearch / PG 全文索引）
List<Document> kwHits = esClient.bm25Search(q, 50);

// 3. RRF 融合（本仓库的 rrfFuse 可直接复用）
List<String> fused = HybridRetriever.rrfFuse(ids(vecHits), ids(kwHits), 60);

// 4. Rerank 精排（BGE-reranker-v2 / Cohere / Jina）
List<String> top = reranker.rerank(q, fused, 5);
```

## 📐 指标定义

- **HitRate@K**：前 K 条结果里包含任一 `expected_doc_id` 的题目占比
- **无答案题**（10 题）不计入命中率，用于单独评估**幻觉率**（系统是否会在无答案时硬答）

## ⚠️ 说明

- 本目录是**评估框架 + 教学实现**：`HybridRetriever` 用内存计算模拟两路召回，目的是让评估流程可离线跑通（无需向量库、无需 API Key）
- 真实效果取决于你的语料、Embedding、Rerank 模型
