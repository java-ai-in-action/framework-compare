package com.javaai.rag;

import java.util.*;

/**
 * 混合检索：向量召回 + 关键词召回 → RRF 融合 →（可选）Rerank 精排
 *
 * ⚠️ 本类内置"内存版"实现，让读者无需向量库 / API Key 即可跑通评估流程。
 *    生产环境请替换为：
 *      - 向量召回：Spring AI VectorStore.similaritySearch()
 *      - 关键词召回：Elasticsearch / Lucene / PostgreSQL 全文索引
 *      - Rerank 精排：BGE-reranker-v2 / Cohere Rerank / Jina Rerank
 *
 * 核心思想（篇4 结论）：召回要广（top-50），精排要准（top-5）。
 */
public class HybridRetriever {

    private final List<Models.Doc> docs;

    public HybridRetriever(List<Models.Doc> docs) {
        this.docs = docs;
    }

    /** 主入口：混合检索，返回 topK 个文档 id */
    public List<String> retrieve(String query, int topK) {
        List<String> vecHits = vectorRecall(query, 50);   // 语义层召回（模拟：title + keywords）
        List<String> kwHits = keywordRecall(query, 50);   // 关键词层召回（模拟：全文）
        List<String> fused = rrfFuse(vecHits, kwHits, 60); // RRF 融合

        // 真实场景：fused = reranker.rerank(query, fused, topK);
        return fused.stream().limit(topK).toList();
    }

    /** 模拟向量召回：只看 title + keywords（近似"语义摘要"） */
    private List<String> vectorRecall(String query, int topK) {
        Set<String> q = tokenize(query);
        return docs.stream()
                .map(d -> Map.entry(d.id(), overlap(q, tokenize(d.title() + " " + String.join(" ", d.keywords())))))
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topK)
                .map(Map.Entry::getKey)
                .toList();
    }

    /** 模拟关键词召回：看全文 content */
    private List<String> keywordRecall(String query, int topK) {
        Set<String> q = tokenize(query);
        return docs.stream()
                .map(d -> Map.entry(d.id(), overlap(q, tokenize(d.content()))))
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topK)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * RRF（Reciprocal Rank Fusion）：只看排名，对分数尺度不敏感，比加权平均鲁棒。
     * score(doc) = Σ 1 / (k + rank)，k 常取 60。
     */
    static List<String> rrfFuse(List<String> a, List<String> b, int k) {
        Map<String, Double> score = new HashMap<>();
        for (int i = 0; i < a.size(); i++) {
            score.merge(a.get(i), 1.0 / (k + i + 1), Double::sum);
        }
        for (int i = 0; i < b.size(); i++) {
            score.merge(b.get(i), 1.0 / (k + i + 1), Double::sum);
        }
        return score.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
    }

    /** 简陋分词：中文按 2-gram，英文/数字按词（长度 ≥2） */
    static Set<String> tokenize(String text) {
        Set<String> tokens = new HashSet<>();
        if (text == null) return tokens;
        String t = text.toLowerCase();

        for (String w : t.split("[^a-z0-9]+")) {
            if (w.length() >= 2) tokens.add(w);
        }

        StringBuilder han = new StringBuilder();
        for (char c : t.toCharArray()) {
            if (c >= 0x4e00 && c <= 0x9fff) {
                han.append(c);
            } else {
                addBigrams(han.toString(), tokens);
                han.setLength(0);
            }
        }
        addBigrams(han.toString(), tokens);
        return tokens;
    }

    private static void addBigrams(String s, Set<String> out) {
        for (int i = 0; i + 1 < s.length(); i++) {
            out.add(s.substring(i, i + 2));
        }
    }

    private static double overlap(Set<String> q, Set<String> d) {
        if (q.isEmpty()) return 0;
        int hit = 0;
        for (String t : q) if (d.contains(t)) hit++;
        return (double) hit / q.size();
    }
}
